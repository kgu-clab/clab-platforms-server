package page.clab.api.global.auth.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import page.clab.api.global.common.dto.ResponseModel;
import page.clab.api.global.util.HttpReqResUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
public class CustomBasicAuthenticationFilter extends BasicAuthenticationFilter {

    private final RedisIpAccessMonitorService redisIpAccessMonitorService;

    private final BlacklistIpRepository blacklistIpRepository;

    private final boolean whitelistEnabled;

    private final String whitelistPath;

    private static final String[] SWAGGER_PATTERNS = {
            "/v2/api-docs",
            "/v3/api-docs",
            "/swagger-resources",
            "/swagger-resources/.*",
            "/swagger-ui.html",
            "/v3/api-docs/.*",
            "/swagger-ui/.*"
    };

    public CustomBasicAuthenticationFilter(
            AuthenticationManager authenticationManager,
            RedisIpAccessMonitorService redisIpAccessMonitorService,
            BlacklistIpRepository blacklistIpRepository,
            boolean whitelistEnabled,
            String whitelistPath
    ) {
        super(authenticationManager);
        this.redisIpAccessMonitorService = redisIpAccessMonitorService;
        this.blacklistIpRepository = blacklistIpRepository;
        this.whitelistEnabled = whitelistEnabled;
        this.whitelistPath = whitelistPath;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String path = request.getRequestURI();
        if (!isSwaggerRequest(path)) {
            chain.doFilter(request, response);
            return;
        }
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        if (whitelistEnabled) {
            List<String> whitelistIps = loadWhitelistIps();
            if (whitelistIps != null && !whitelistIps.contains(clientIpAddress) && !whitelistIps.contains("*")) {
                log.info("[{}] : 화이트리스트에 등록되지 않은 IP입니다.", clientIpAddress);
                ResponseModel responseModel = ResponseModel.builder()
                        .success(false)
                        .build();
                response.getWriter().write(responseModel.toJson());
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        if (blacklistIpRepository.existsByIpAddress(clientIpAddress) || redisIpAccessMonitorService.isBlocked(clientIpAddress)) {
            log.info("[{}] : 정책에 의해 차단된 IP입니다.", clientIpAddress);
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

    private List<String> loadWhitelistIps() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Path path = Paths.get(whitelistPath);
            if (Files.notExists(path)) {
                Files.createDirectories(path.getParent());
                Map<String, List<String>> defaultContent = Map.of("allowedIps", List.of("*"));
                mapper.writeValue(Files.newBufferedWriter(path), defaultContent);
            }
            Map<String, List<String>> data = mapper.readValue(path.toFile(), new TypeReference<>() {});
            return data.get("allowedIps");
        } catch (IOException e) {
            log.error("Failed to load or create IP whitelist from path: {}", whitelistPath, e);
            return null;
        }
    }

}