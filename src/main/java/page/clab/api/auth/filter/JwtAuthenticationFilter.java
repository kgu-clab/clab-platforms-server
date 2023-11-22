package page.clab.api.auth.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import page.clab.api.auth.jwt.JwtTokenProvider;
import page.clab.api.repository.BlacklistIpRepository;
import page.clab.api.service.RedisTokenService;
import page.clab.api.type.entity.RedisToken;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisTokenService redisTokenService;
    
    private final BlacklistIpRepository blacklistIpRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String clientIpAddress = request.getRemoteAddr();
        log.debug("clientIpAddress : {}", clientIpAddress);
        if (blacklistIpRepository.existsByIpAddress(clientIpAddress)) {
            throw new SecurityException("[" + clientIpAddress + "] 서비스 이용 불가 IP입니다.");
        }
        String accessToken = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        if (accessToken == null) {
            throw new SecurityException("토큰 정보가 없습니다.");
        }
        RedisToken redisToken = redisTokenService.getRedisToken(accessToken);
        if (redisToken == null) {
            throw new SecurityException("존재하지 않는 토큰입니다.");
        }
        if (!redisToken.getIp().equals(clientIpAddress)) {
            redisTokenService.deleteRedisTokenByAccessToken(accessToken);
            throw new SecurityException("[" + clientIpAddress + "] 토큰 발급 IP와 다른 IP에서 접속하여 토큰을 삭제하였습니다.");
        }
        if (!((HttpServletRequest) request).getRequestURI().equals("/login/reissue") && jwtTokenProvider.validateToken(accessToken)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

}