package page.clab.api.global.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import page.clab.api.domain.blacklistIp.dao.BlacklistIpRepository;
import page.clab.api.global.auth.application.RedisIpAccessMonitorService;
import page.clab.api.global.auth.application.WhitelistService;
import page.clab.api.global.util.HttpReqResUtil;
import page.clab.api.global.util.ResponseUtil;
import page.clab.api.global.util.SwaggerUtil;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Slf4j
public class CustomBasicAuthenticationFilter extends BasicAuthenticationFilter {

    private final RedisIpAccessMonitorService redisIpAccessMonitorService;

    private final BlacklistIpRepository blacklistIpRepository;

    private final WhitelistService whitelistService;

    public CustomBasicAuthenticationFilter(
            AuthenticationManager authenticationManager,
            RedisIpAccessMonitorService redisIpAccessMonitorService,
            BlacklistIpRepository blacklistIpRepository,
            WhitelistService whitelistService
    ) {
        super(authenticationManager);
        this.redisIpAccessMonitorService = redisIpAccessMonitorService;
        this.blacklistIpRepository = blacklistIpRepository;
        this.whitelistService = whitelistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String path = request.getRequestURI();
        if (!SwaggerUtil.isSwaggerRequest(path)) {
            chain.doFilter(request, response);
            return;
        }
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        List<String> whitelistIps = whitelistService.loadWhitelistIps();
        if (!whitelistIps.contains(clientIpAddress) && !whitelistIps.contains("*")) {
            log.info("[{}] : 화이트리스트에 등록되지 않은 IP입니다.", clientIpAddress);
            ResponseUtil.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (blacklistIpRepository.existsByIpAddress(clientIpAddress) || redisIpAccessMonitorService.isBlocked(clientIpAddress)) {
            log.info("[{}] : 정책에 의해 차단된 IP입니다.", clientIpAddress);
            ResponseUtil.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Basic ")) {
            response.setHeader("WWW-Authenticate", "Basic realm=\"Please enter your username and password\"");
            ResponseUtil.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String base64Credentials = authorizationHeader.substring("Basic ".length());
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        String[] values = credentials.split(":", 2);
        if (values.length < 2) {
            ResponseUtil.sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String username = values[0];
        String password = values[1];
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = getAuthenticationManager().authenticate(authRequest);
        if (authentication == null) {
            ResponseUtil.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        super.doFilterInternal(request, response, chain);
    }

}