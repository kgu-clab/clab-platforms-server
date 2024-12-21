package page.clab.api.global.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import page.clab.api.domain.auth.login.domain.RedisToken;
import page.clab.api.external.auth.blacklistIp.application.port.ExternalRetrieveBlacklistIpUseCase;
import page.clab.api.external.auth.redisIpAccessMonitor.application.port.ExternalCheckIpBlockedUseCase;
import page.clab.api.external.auth.redisToken.application.port.ExternalManageRedisTokenUseCase;
import page.clab.api.global.auth.application.JwtTokenService;
import page.clab.api.global.common.notificationSetting.application.event.NotificationEvent;
import page.clab.api.global.common.notificationSetting.domain.SecurityAlertType;
import page.clab.api.global.util.HttpReqResUtil;
import page.clab.api.global.util.ResponseUtil;
import page.clab.api.global.util.WhitelistPathMatcher;

/**
 * {@code JwtAuthenticationFilter}는 JWT 토큰을 검증하고, IP 주소 기반 접근 제한을 수행하는 필터입니다.
 *
 * <p>이 필터는 다음과 같은 보안 검증을 수행합니다:</p>
 * <ul>
 *     <li>화이트리스트 경로 확인: 화이트리스트에 있는 요청 경로에 대해서는 필터링을 생략</li>
 *     <li>IP 주소 검증: 블랙리스트에 포함된 IP 주소이거나 차단된 IP인 경우 접근 차단</li>
 *     <li>JWT 토큰 검증: 유효한 토큰인지 확인하고, 관리자 토큰의 경우 발급된 IP와 동일한지 검증</li>
 * </ul>
 *
 * <p>특히, 관리자 토큰이 발급된 IP와 다른 IP로부터의 접근이 발생하면 해당 토큰을 삭제하고,
 * Slack을 통해 보안 경고 메시지를 전송합니다.</p>
 *
 * <p>검증 실패 시, 401 Unauthorized 응답을 반환하여 접근을 제한합니다.</p>
 *
 * @see GenericFilterBean
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenService jwtTokenService;
    private final ApplicationEventPublisher eventPublisher;
    private final ExternalManageRedisTokenUseCase externalManageRedisTokenUseCase;
    private final ExternalCheckIpBlockedUseCase externalCheckIpBlockedUseCase;
    private final ExternalRetrieveBlacklistIpUseCase externalRetrieveBlacklistIpUseCase;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String path = httpServletRequest.getRequestURI();
        if (WhitelistPathMatcher.isWhitelistRequest(path)) {
            chain.doFilter(request, response);
            return;
        }
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        if (!verifyIpAddressAccess(httpServletResponse, clientIpAddress)) {
            return;
        }
        if (!authenticateToken(httpServletRequest, httpServletResponse, clientIpAddress)) {
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean verifyIpAddressAccess(HttpServletResponse response, String clientIpAddress) throws IOException {
        if (externalRetrieveBlacklistIpUseCase.existsByIpAddress(clientIpAddress)
            || externalCheckIpBlockedUseCase.isIpBlocked(clientIpAddress)) {
            log.info("[{}] : 서비스 이용이 제한된 IP입니다.", clientIpAddress);
            ResponseUtil.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }

    private boolean authenticateToken(HttpServletRequest request, HttpServletResponse response, String clientIpAddress)
        throws IOException {
        String token = jwtTokenService.resolveToken(request);

        // 토큰이 존재하고 유효한 경우
        if (token != null && jwtTokenService.validateToken(token)) {
            RedisToken redisToken = externalManageRedisTokenUseCase.findByAccessToken(token);
            if (redisToken == null) {
                log.warn("존재하지 않는 토큰입니다.");
                ResponseUtil.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }

            // 관리자 토큰이고, 토큰 발급 IP와 다른 IP에서 접속한 경우 토큰 삭제
            if (redisToken.isAdminToken() && !redisToken.isSameIp(clientIpAddress)) {
                externalManageRedisTokenUseCase.deleteByAccessToken(token);
                sendSecurityAlertSlackMessage(request, redisToken);
                log.warn("[{}] 토큰 발급 IP와 다른 IP에서 접속하여 토큰을 삭제하였습니다.", clientIpAddress);
                ResponseUtil.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            Authentication authentication = jwtTokenService.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        return true;
    }

    private void sendSecurityAlertSlackMessage(HttpServletRequest request, RedisToken redisToken) {
        if (redisToken.isAdminToken()) {
            request.setAttribute("member", redisToken.getId());
            String duplicateLoginMessage = "토큰 발급 IP와 다른 IP에서 접속하여 토큰을 삭제하였습니다.";
            eventPublisher.publishEvent(
                new NotificationEvent(this, SecurityAlertType.DUPLICATE_LOGIN, request, duplicateLoginMessage));
        }
    }
}
