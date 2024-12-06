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
import page.clab.api.domain.auth.login.domain.RedisToken;
import page.clab.api.external.auth.blacklistIp.application.port.ExternalRetrieveBlacklistIpUseCase;
import page.clab.api.external.auth.redisIpAccessMonitor.application.port.ExternalCheckIpBlockedUseCase;
import page.clab.api.external.auth.redisToken.application.port.ExternalManageRedisTokenUseCase;
import page.clab.api.global.auth.jwt.JwtTokenProvider;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;
import page.clab.api.global.util.HttpReqResUtil;
import page.clab.api.global.util.ResponseUtil;
import page.clab.api.global.util.WhitelistPathMatcher;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final SlackService slackService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ExternalManageRedisTokenUseCase externalManageRedisTokenUseCase;
    private final ExternalCheckIpBlockedUseCase externalCheckIpBlockedUseCase;
    private final ExternalRetrieveBlacklistIpUseCase externalRetrieveBlacklistIpUseCase;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
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
        if (externalRetrieveBlacklistIpUseCase.existsByIpAddress(clientIpAddress) || externalCheckIpBlockedUseCase.isIpBlocked(clientIpAddress)) {
            log.info("[{}] : 서비스 이용이 제한된 IP입니다.", clientIpAddress);
            ResponseUtil.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }

    private boolean authenticateToken(HttpServletRequest request, HttpServletResponse response, String clientIpAddress) throws IOException {
        String token = jwtTokenProvider.resolveToken(request);

        // 토큰이 존재하고 유효한 경우
        if (token != null && jwtTokenProvider.validateToken(token)) {
            RedisToken redisToken = jwtTokenProvider.isRefreshToken(token) ? externalManageRedisTokenUseCase.findByRefreshToken(token) : externalManageRedisTokenUseCase.findByAccessToken(token);
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
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        return true;
    }

    private void sendSecurityAlertSlackMessage(HttpServletRequest request, RedisToken redisToken) {
        if (redisToken.isAdminToken()) {
            request.setAttribute("member", redisToken.getId());
            slackService.sendSecurityAlertNotification(request, SecurityAlertType.DUPLICATE_LOGIN, "토큰 발급 IP와 다른 IP에서 접속하여 토큰을 삭제하였습니다.");
        }
    }
}
