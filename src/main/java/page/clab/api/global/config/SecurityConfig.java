package page.clab.api.global.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import page.clab.api.external.auth.blacklistIp.application.port.ExternalRegisterBlacklistIpUseCase;
import page.clab.api.external.auth.blacklistIp.application.port.ExternalRetrieveBlacklistIpUseCase;
import page.clab.api.external.auth.redisIpAccessMonitor.application.port.ExternalCheckIpBlockedUseCase;
import page.clab.api.external.auth.redisIpAccessMonitor.application.port.ExternalRegisterIpAccessMonitorUseCase;
import page.clab.api.external.auth.redisToken.application.port.ExternalManageRedisTokenUseCase;
import page.clab.api.global.auth.application.WhitelistService;
import page.clab.api.global.auth.filter.CustomBasicAuthenticationFilter;
import page.clab.api.global.auth.filter.InvalidEndpointAccessFilter;
import page.clab.api.global.auth.filter.IpAuthenticationFilter;
import page.clab.api.global.auth.filter.JwtAuthenticationFilter;
import page.clab.api.global.auth.jwt.JwtTokenProvider;
import page.clab.api.global.common.file.application.FileService;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.filter.IPinfoSpringFilter;
import page.clab.api.global.util.ApiLogger;
import page.clab.api.global.util.HttpReqResUtil;
import page.clab.api.global.util.ResponseUtil;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final ExternalManageRedisTokenUseCase externalManageRedisTokenUseCase;
    private final ExternalRegisterIpAccessMonitorUseCase externalRegisterIpAccessMonitorUseCase;
    private final ExternalCheckIpBlockedUseCase externalCheckIpBlockedUseCase;
    private final ExternalRegisterBlacklistIpUseCase externalRegisterBlacklistIpUseCase;
    private final ExternalRetrieveBlacklistIpUseCase externalRetrieveBlacklistIpUseCase;
    private final SlackService slackService;
    private final WhitelistService whitelistService;
    private final WhitelistAccountProperties whitelistAccountProperties;
    private final WhitelistPatternsProperties whitelistPatternsProperties;
    private final IPInfoConfig ipInfoConfig;
    private final AuthenticationConfig authenticationConfig;
    private final CorsConfigurationSource corsConfigurationSource;
    private final JwtTokenProvider jwtTokenProvider;
    private final FileService fileService;
    private final ApiLogger apiLogger;

    @Value("${resource.file.url}")
    String fileURL;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = authenticationConfig.authenticationManager();
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .cors(cors ->
                        cors.configurationSource(corsConfigurationSource)
                )
                .authorizeHttpRequests(
                        getAuthorizeHttpRequestsCustomizer()
                )
                .authenticationProvider(authenticationConfig.authenticationProvider())
                .addFilterBefore(
                        new IPinfoSpringFilter(ipInfoConfig),
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(
                        new IpAuthenticationFilter(ipInfoConfig),
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(
                        new InvalidEndpointAccessFilter(slackService, fileURL, externalRegisterBlacklistIpUseCase, externalRetrieveBlacklistIpUseCase),
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(
                        new CustomBasicAuthenticationFilter(authenticationManager, whitelistService, slackService, externalCheckIpBlockedUseCase, externalRetrieveBlacklistIpUseCase),
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(slackService, jwtTokenProvider, externalManageRedisTokenUseCase, externalCheckIpBlockedUseCase, externalRetrieveBlacklistIpUseCase),
                        UsernamePasswordAuthenticationFilter.class
                )
//                .addFilterBefore(
//                        new FileAccessControlFilter(fileService, fileURL),
//                        UsernamePasswordAuthenticationFilter.class
//                )
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                        httpSecurityExceptionHandlingConfigurer
                                .authenticationEntryPoint(this::handleException)
                                .accessDeniedHandler(this::handleException)
                );
        return http.build();
    }

    private @NotNull Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> getAuthorizeHttpRequestsCustomizer() {
        return (authorize) -> authorize
                .requestMatchers(SecurityConstants.PERMIT_ALL).permitAll()
                .requestMatchers(HttpMethod.GET, SecurityConstants.PERMIT_ALL_API_ENDPOINTS_GET).permitAll()
                .requestMatchers(HttpMethod.POST, SecurityConstants.PERMIT_ALL_API_ENDPOINTS_POST).permitAll()
                .requestMatchers(whitelistPatternsProperties.getWhitelistPatterns()).hasRole(whitelistAccountProperties.getRole())
                .anyRequest().authenticated();
    }

    private void handleException(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException {
        String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
        String message;
        int statusCode;

        if (exception instanceof AuthenticationException) {
            message = "인증되지 않은 사용자의 비정상적인 접근이 감지되었습니다.";
            statusCode = HttpServletResponse.SC_UNAUTHORIZED;
        } else if (exception instanceof AccessDeniedException) {
            message = "권한이 없는 엔드포인트에 대한 접근이 감지되었습니다.";
            statusCode = HttpServletResponse.SC_FORBIDDEN;
        } else {
            message = "비정상적인 접근이 감지되었습니다.";
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
        }

        apiLogger.logRequest(request, response, clientIpAddress, message);
        externalRegisterIpAccessMonitorUseCase.registerIpAccessMonitor(request, clientIpAddress);
        ResponseUtil.sendErrorResponse(response, statusCode);
    }
}
