package page.clab.api.global.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import page.clab.api.domain.blacklistIp.dao.BlacklistIpRepository;
import page.clab.api.domain.login.application.RedisTokenService;
import page.clab.api.domain.login.domain.RedisToken;
import page.clab.api.global.auth.application.RedisIpAttemptService;
import page.clab.api.global.auth.jwt.JwtTokenProvider;
import page.clab.api.global.common.dto.ResponseModel;
import page.clab.api.global.util.HttpReqResUtil;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisTokenService redisTokenService;

    private final RedisIpAttemptService redisIpAttemptService;

    private final BlacklistIpRepository blacklistIpRepository;

    private static final String[] SWAGGER_PATTERNS = {
            "/v2/api-docs",
            "/v3/api-docs",
            "/swagger-resources",
            "/swagger-resources/.*",
            "/swagger-ui.html",
            "/v3/api-docs/.*",
            "/swagger-ui/.*"
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String path = ((HttpServletRequest) request).getRequestURI();
        if (isSwaggerRequest(path)) {
            chain.doFilter(request, response);
            return;
        }
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        if (blacklistIpRepository.existsByIpAddress(clientIpAddress) || redisIpAttemptService.isBlocked(clientIpAddress)) {
            log.info("[{}] : 서비스 이용이 제한된 IP입니다.", clientIpAddress);
            ResponseModel responseModel = ResponseModel.builder()
                    .success(false)
                    .build();
            response.getWriter().write(responseModel.toJson());
            response.setContentType("application/json");
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            RedisToken redisToken = (jwtTokenProvider.isRefreshToken(token)) ? redisTokenService.getRedisTokenByRefreshToken(token) : redisTokenService.getRedisTokenByAccessToken(token);
            if (redisToken == null) {
                throw new SecurityException("존재하지 않는 토큰입니다.");
            }
            if (!redisToken.getIp().equals(clientIpAddress)) {
                redisTokenService.deleteRedisTokenByAccessToken(token);
                throw new SecurityException("[" + clientIpAddress + "] 토큰 발급 IP와 다른 IP에서 접속하여 토큰을 삭제하였습니다.");
            }
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private boolean isSwaggerRequest(String path) {
        for (String pattern : SWAGGER_PATTERNS) {
            if (Pattern.compile(pattern).matcher(path).find()) {
                return true;
            }
        }
        return false;
    }

}