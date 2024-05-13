package page.clab.api.global.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import page.clab.api.domain.blacklistIp.dao.BlacklistIpRepository;
import page.clab.api.domain.login.application.RedisTokenService;
import page.clab.api.domain.login.domain.RedisToken;
import page.clab.api.global.auth.application.RedisIpAccessMonitorService;
import page.clab.api.global.auth.jwt.JwtTokenProvider;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;
import page.clab.api.global.util.HttpReqResUtil;
import page.clab.api.global.util.ResponseUtil;
import page.clab.api.global.util.SwaggerUtil;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisTokenService redisTokenService;

    private final RedisIpAccessMonitorService redisIpAccessMonitorService;

    private final SlackService slackService;

    private final BlacklistIpRepository blacklistIpRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String path = httpServletRequest.getRequestURI();
        if (SwaggerUtil.isSwaggerRequest(path)) {
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
        if (blacklistIpRepository.existsByIpAddress(clientIpAddress) || redisIpAccessMonitorService.isBlocked(clientIpAddress)) {
            log.info("[{}] : 서비스 이용이 제한된 IP입니다.", clientIpAddress);
            ResponseUtil.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }

    private boolean authenticateToken(HttpServletRequest request, HttpServletResponse response, String clientIpAddress) throws IOException {
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            RedisToken redisToken = jwtTokenProvider.isRefreshToken(token) ? redisTokenService.getRedisTokenByRefreshToken(token) : redisTokenService.getRedisTokenByAccessToken(token);
            if (redisToken == null) {
                log.warn("존재하지 않는 토큰입니다.");
                ResponseUtil.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            if (!redisToken.getIp().equals(clientIpAddress)) {
                redisTokenService.deleteRedisTokenByAccessToken(token);
                sendSlackMessage(request, redisToken);
                log.warn("[{}] 토큰 발급 IP와 다른 IP에서 접속하여 토큰을 삭제하였습니다.", clientIpAddress);
                ResponseUtil.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        } else {
            return true;
        }
    }

    private void sendSlackMessage(HttpServletRequest request, RedisToken redisToken) {
        if (redisToken.isAdminToken()) {
            request.setAttribute("memberId", redisToken.getId());
            slackService.sendSecurityAlertNotification(request, SecurityAlertType.DUPLICATE_LOGIN, "토큰 발급 IP와 다른 IP에서 접속하여 토큰을 삭제하였습니다.");
        }
    }

}