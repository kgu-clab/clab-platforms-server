package page.clab.api.global.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import page.clab.api.domain.blacklistIp.dao.BlacklistIpRepository;
import page.clab.api.global.auth.application.RedisIpAccessMonitorService;
import page.clab.api.global.auth.application.WhitelistService;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;
import page.clab.api.global.util.HttpReqResUtil;
import page.clab.api.global.util.ResponseUtil;
import page.clab.api.global.util.WhitelistUtil;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Component
@Slf4j
public class CustomBasicAuthenticationFilter extends BasicAuthenticationFilter {

    private final RedisIpAccessMonitorService redisIpAccessMonitorService;
    private final BlacklistIpRepository blacklistIpRepository;
    private final WhitelistService whitelistService;
    private final SlackService slackService;

    public CustomBasicAuthenticationFilter(
            AuthenticationManager authenticationManager,
            RedisIpAccessMonitorService redisIpAccessMonitorService,
            BlacklistIpRepository blacklistIpRepository,
            WhitelistService whitelistService,
            SlackService slackService
    ) {
        super(authenticationManager);
        this.redisIpAccessMonitorService = redisIpAccessMonitorService;
        this.blacklistIpRepository = blacklistIpRepository;
        this.whitelistService = whitelistService;
        this.slackService = slackService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String path = request.getRequestURI();
        if (!WhitelistUtil.isWhitelistRequest(path)) {
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

    private boolean authenticateUserCredentials(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
        List<String> whitelistIps = whitelistService.loadWhitelistIps();
        if (!(whitelistIps.contains(clientIpAddress) || whitelistIps.contains("*")) ||
                blacklistIpRepository.existsByIpAddress(clientIpAddress) ||
                redisIpAccessMonitorService.isBlocked(clientIpAddress)
        ) {
            log.info("[{}] : 정책에 의해 차단된 IP입니다.", clientIpAddress);
            ResponseUtil.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }

    @NotNull
    private static String[] decodeCredentials(String authorizationHeader) {
        String base64Credentials = authorizationHeader.substring("Basic ".length());
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        return credentials.split(":", 2);
    }

    private void sendAuthenticationSuccessAlertSlackMessage(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (WhitelistUtil.isSwaggerIndexEndpoint(path)) {
            slackService.sendSecurityAlertNotification(request, SecurityAlertType.API_DOCS_ACCESS,"API 문서에 대한 접근이 허가되었습니다.");
        } else if (WhitelistUtil.isActuatorRequest(path)) {
            slackService.sendSecurityAlertNotification(request, SecurityAlertType.ACTUATOR_ACCESS,"Actuator에 대한 접근이 허가되었습니다.");
        }
    }

    private void sendAuthenticationFailureAlertSlackMessage(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (WhitelistUtil.isSwaggerIndexEndpoint(path)) {
            slackService.sendSecurityAlertNotification(request, SecurityAlertType.API_DOCS_ACCESS,"API 문서에 대한 접근이 거부되었습니다.");
        } else if (WhitelistUtil.isActuatorRequest(path)) {
            slackService.sendSecurityAlertNotification(request, SecurityAlertType.ACTUATOR_ACCESS,"Actuator에 대한 접근이 거부되었습니다.");
        }
    }
}