package page.clab.api.global.auth.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import page.clab.api.domain.blacklistIp.dao.BlacklistIpRepository;
import page.clab.api.global.auth.application.RedisIpAccessMonitorService;
import page.clab.api.global.auth.application.WhitelistService;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.util.HttpReqResUtil;
import page.clab.api.global.util.ResponseUtil;
import page.clab.api.global.util.SwaggerUtil;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Slf4j
public class ApiDocsAuthenticationFilter extends BasicAuthenticationFilter {

    private final WhitelistService whitelistService;

    private final RedisIpAccessMonitorService redisIpAccessMonitorService;

    private final BlacklistIpRepository blacklistIpRepository;

    private final SlackService slackService;

    private boolean isSwaggerAccessed = false;

    private boolean isAuthenticationSuccess = false;

    public ApiDocsAuthenticationFilter(
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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        if (SwaggerUtil.isSwaggerRequest(path)) {
            if (!isSwaggerAccessed) {
                log.info("Swagger 접근이 감지되었습니다 : {}", clientIpAddress);
                isSwaggerAccessed = true;
            }
            if (verifyIpAddressAccess(response)) {
                if (request.getHeader("Authorization") != null) {
                    handleSwaggerAuthentication(request, response, filterChain, clientIpAddress);
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
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

    private void handleSwaggerAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String clientIpAddress) throws IOException, ServletException {
        if (authenticateUserCredentials(request, response)) {
            if(!isAuthenticationSuccess) {
                slackService.sendSwaggerAccessNotification(request, "성공");
                isAuthenticationSuccess = true;
            }
            filterChain.doFilter(request, response);
        }else{
            slackService.sendSwaggerAccessNotification(request, "실패");
        }

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
            return true;
        } catch (Exception e) {
            ResponseUtil.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    @NotNull
    private static String[] decodeCredentials(String authorizationHeader) {
        String base64Credentials = authorizationHeader.substring("Basic ".length());
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        return credentials.split(":", 2);
    }

}

