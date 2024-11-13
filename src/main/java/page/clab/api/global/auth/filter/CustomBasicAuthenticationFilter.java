package page.clab.api.global.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import page.clab.api.external.auth.blacklistIp.application.port.ExternalRetrieveBlacklistIpUseCase;
import page.clab.api.external.auth.redisIpAccessMonitor.application.port.ExternalCheckIpBlockedUseCase;
import page.clab.api.global.auth.util.IpWhitelistValidator;
import page.clab.api.global.common.notificationSetting.application.event.NotificationEvent;
import page.clab.api.global.common.notificationSetting.domain.SecurityAlertType;
import page.clab.api.global.util.HttpReqResUtil;
import page.clab.api.global.util.ResponseUtil;
import page.clab.api.global.util.WhitelistPathMatcher;

/**
 * {@code CustomBasicAuthenticationFilter}는 기본 인증 필터를 확장하여 추가적인 보안 기능을 제공합니다.
 *
 * <p>IP 주소 기반 접근 제한, 화이트리스트 경로 검증, 사용자 인증 정보를 바탕으로
 * Slack 보안 알림을 전송하는 기능을 포함합니다. 또한 Swagger 또는 Actuator에 대한 접근이 성공하거나 실패할 경우 이를 Slack에 알립니다.</p>
 *
 * <p>이 필터는 다음과 같은 추가 검증을 수행합니다:</p>
 * <ul>
 *     <li>화이트리스트 경로 검증: 화이트리스트에 있는 경로에 대해서만 필터링 수행</li>
 *     <li>IP 주소 검증: 허용된 IP 주소인지, 블랙리스트에 포함되어 있는지, IP가 차단되어 있는지 확인</li>
 *     <li>사용자 인증: Basic Authentication 헤더를 확인하고 인증을 수행</li>
 * </ul>
 *
 * <p>IP 또는 인증 정보가 유효하지 않은 경우, 응답으로 401 Unauthorized를 반환합니다.
 * 또한 인증 성공 또는 실패에 대한 정보를 Slack 보안 채널로 전송합니다.</p>
 *
 * @see BasicAuthenticationFilter
 */
@Slf4j
public class CustomBasicAuthenticationFilter extends BasicAuthenticationFilter {

    private final IpWhitelistValidator ipWhitelistValidator;
    private final ExternalCheckIpBlockedUseCase externalCheckIpBlockedUseCase;
    private final ExternalRetrieveBlacklistIpUseCase externalRetrieveBlacklistIpUseCase;
    private final ApplicationEventPublisher eventPublisher;

    public CustomBasicAuthenticationFilter(
            AuthenticationManager authenticationManager,
            IpWhitelistValidator ipWhitelistValidator,
            ExternalCheckIpBlockedUseCase externalCheckIpBlockedUseCase,
            ExternalRetrieveBlacklistIpUseCase externalRetrieveBlacklistIpUseCase,
            ApplicationEventPublisher eventPublisher
    ) {
        super(authenticationManager);
        this.externalCheckIpBlockedUseCase = externalCheckIpBlockedUseCase;
        this.externalRetrieveBlacklistIpUseCase = externalRetrieveBlacklistIpUseCase;
        this.ipWhitelistValidator = ipWhitelistValidator;
        this.eventPublisher = eventPublisher;
    }

    @NotNull
    private static String[] decodeCredentials(String authorizationHeader) {
        String base64Credentials = authorizationHeader.substring("Basic ".length());
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        return credentials.split(":", 2);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String path = request.getRequestURI();
        if (!WhitelistPathMatcher.isWhitelistRequest(path)) {
            chain.doFilter(request, response);
            return;
        }
        if (!verifyIpAddressAccess(response)) {
            return;
        }
        if (!authenticateUserCredentials(request, response)) {
            return;
        }
        super.doFilterInternal(request, response, chain);
    }

    private boolean authenticateUserCredentials(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Basic ")) {
            response.setHeader("WWW-Authenticate", "Basic realm=\"Please enter your username and password\"");
            ResponseUtil.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        String[] credentials = decodeCredentials(authorizationHeader);
        if (credentials.length < 2) {
            ResponseUtil.sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }
        String username = credentials[0];
        String password = credentials[1];
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication authentication = getAuthenticationManager().authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            sendAuthenticationSuccessAlertSlackMessage(request);
        } catch (BadCredentialsException e) {
            sendAuthenticationFailureAlertSlackMessage(request);
            ResponseUtil.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }

    private boolean verifyIpAddressAccess(HttpServletResponse response) throws IOException {
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        if (!ipWhitelistValidator.isIpWhitelisted(clientIpAddress) ||
                externalRetrieveBlacklistIpUseCase.existsByIpAddress(clientIpAddress) ||
                externalCheckIpBlockedUseCase.isIpBlocked(clientIpAddress)
        ) {
            log.info("[{}] : 정책에 의해 차단된 IP입니다.", clientIpAddress);
            ResponseUtil.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }

    private void sendAuthenticationSuccessAlertSlackMessage(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (WhitelistPathMatcher.isSwaggerIndexEndpoint(path)) {
            String additionalMessage = "API 문서에 대한 접근이 허가되었습니다.";
            eventPublisher.publishEvent(
                    new NotificationEvent(this, SecurityAlertType.API_DOCS_ACCESS, request, additionalMessage));
        } else if (WhitelistPathMatcher.isActuatorRequest(path)) {
            String additionalMessage = "Actuator에 대한 접근이 허가되었습니다.";
            eventPublisher.publishEvent(
                    new NotificationEvent(this, SecurityAlertType.ACTUATOR_ACCESS, request, additionalMessage));
        }
    }

    private void sendAuthenticationFailureAlertSlackMessage(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (WhitelistPathMatcher.isSwaggerIndexEndpoint(path)) {
            String additionalMessage = "API 문서에 대한 접근이 거부되었습니다.";
            eventPublisher.publishEvent(
                    new NotificationEvent(this, SecurityAlertType.API_DOCS_ACCESS, request, additionalMessage));
        } else if (WhitelistPathMatcher.isActuatorRequest(path)) {
            String additionalMessage = "Actuator에 대한 접근이 거부되었습니다.";
            eventPublisher.publishEvent(
                    new NotificationEvent(this, SecurityAlertType.ACTUATOR_ACCESS, request, additionalMessage));
        }
    }
}
