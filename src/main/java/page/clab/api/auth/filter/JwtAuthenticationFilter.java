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

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    private final BlacklistIpRepository blacklistIpRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            if (!((HttpServletRequest) request).getRequestURI().equals("/login/reissue")) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            String clientIpAddress = request.getRemoteAddr();
            log.debug("clientIpAddress : {}", clientIpAddress);
            if (blacklistIpRepository.existsByIpAddress(clientIpAddress)) {
                throw new SecurityException("Blacklisted IP address");
            }
        }
        chain.doFilter(request, response);
    }

}