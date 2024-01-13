package page.clab.api.auth.filter;

import java.io.IOException;
import java.util.Base64;
import java.util.regex.Pattern;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import page.clab.api.repository.BlacklistIpRepository;
import page.clab.api.service.RedisIpAttemptService;
import page.clab.api.type.dto.ResponseModel;

@Slf4j
public class CustomBasicAuthenticationFilter extends BasicAuthenticationFilter {

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

    public CustomBasicAuthenticationFilter(AuthenticationManager authenticationManager, RedisIpAttemptService redisIpAttemptService, BlacklistIpRepository blacklistIpRepository) {
        super(authenticationManager);
        this.redisIpAttemptService = redisIpAttemptService;
        this.blacklistIpRepository = blacklistIpRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String path = request.getRequestURI();
        if (!isSwaggerRequest(path)) {
            chain.doFilter(request, response);
            return;
        }
        String clientIpAddress = request.getHeader("X-Forwarded-For");
        if (blacklistIpRepository.existsByIpAddress(clientIpAddress) || redisIpAttemptService.isBlocked(clientIpAddress)) {
            log.info("[{}] : 서비스 이용이 제한된 IP입니다.", clientIpAddress);
            ResponseModel responseModel = ResponseModel.builder()
                    .success(false)
                    .build();
            response.getWriter().write(responseModel.toJson());
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Basic ")) {
            response.setHeader("WWW-Authenticate", "Basic realm=\"Please enter your username and password\"");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }
        String base64Credentials = authorizationHeader.substring("Basic ".length());
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        String[] values = credentials.split(":", 2);
        if (values.length < 2) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid basic authentication token");
            return;
        }
        String username = values[0];
        String password = values[1];
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = getAuthenticationManager().authenticate(authRequest);
        if (authentication == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid username or password");
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        super.doFilterInternal(request, response, chain);
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