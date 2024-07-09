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
import page.clab.api.global.config.ApiDocsConfig;
import page.clab.api.global.config.WhitelistPatternsProperties;
import page.clab.api.global.util.HttpReqResUtil;
import page.clab.api.global.util.ResponseUtil;
import page.clab.api.global.util.WhitelistUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Component
@Slf4j
public class CustomBasicAuthenticationFilter extends BasicAuthenticationFilter {

    private final RedisIpAccessMonitorService redisIpAccessMonitorService;
    private final BlacklistIpRepository blacklistIpRepository;
    private final WhitelistService whitelistService;
    private final WhitelistPatternsProperties whitelistPatternsProperties;
    private final SlackService slackService;
    private final ApiDocsConfig apiDocsConfig;

    public CustomBasicAuthenticationFilter(
            AuthenticationManager authenticationManager,
            RedisIpAccessMonitorService redisIpAccessMonitorService,
            BlacklistIpRepository blacklistIpRepository,
            WhitelistService whitelistService,
            WhitelistPatternsProperties whitelistPatternsProperties,
            SlackService slackService,
            ApiDocsConfig apiDocsConfig
    ) {
        super(authenticationManager);
        this.redisIpAccessMonitorService = redisIpAccessMonitorService;
        this.blacklistIpRepository = blacklistIpRepository;
        this.whitelistService = whitelistService;
        this.whitelistPatternsProperties = whitelistPatternsProperties;
        this.slackService = slackService;
        this.apiDocsConfig = apiDocsConfig;
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
        Authentication authentication = null;
        try {
            authentication = getAuthenticationManager().authenticate(authRequest);
            if (authentication == null) {
                ResponseUtil.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
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

    public boolean isApiDocsPath(String path) {
        String swaggerEndpoint = apiDocsConfig.getSwaggerEndpoint();
        return swaggerEndpoint.equals(path);
    }

    public boolean isActuatorPath(String path) {
        String[] actuatorPaths = whitelistPatternsProperties.getPatterns().get("actuator");
        return Arrays.stream(actuatorPaths)
                .anyMatch(path::matches);
    }

    private void sendAuthenticationSuccessAlertSlackMessage(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (isApiDocsPath(path)) {
            slackService.sendSecurityAlertNotification(request, SecurityAlertType.API_DOCS_ACCESS,"API 문서 접근 성공");
        } else if (isActuatorPath(path)) {
            slackService.sendSecurityAlertNotification(request, SecurityAlertType.ACTUATOR_ACCESS,"Actuator 접근 성공");
        }
    }

    private void sendAuthenticationFailureAlertSlackMessage(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (isApiDocsPath(path)) {
            slackService.sendSecurityAlertNotification(request, SecurityAlertType.API_DOCS_ACCESS,"API 문서 접근 실패");
        } else if (isActuatorPath(path)) {
            slackService.sendSecurityAlertNotification(request, SecurityAlertType.ACTUATOR_ACCESS,"Actuator 접근 실패");
        }
    }
}