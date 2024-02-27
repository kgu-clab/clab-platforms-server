package page.clab.api.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import page.clab.api.domain.blacklistIp.dao.BlacklistIpRepository;
import page.clab.api.domain.login.application.RedisTokenService;
import page.clab.api.global.auth.application.CustomUserDetailsService;
import page.clab.api.global.auth.application.RedisIpAttemptService;
import page.clab.api.global.auth.filter.CustomBasicAuthenticationFilter;
import page.clab.api.global.auth.filter.JwtAuthenticationFilter;
import page.clab.api.global.auth.jwt.JwtTokenProvider;
import page.clab.api.global.common.slack.application.SlackService;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisTokenService redisTokenService;

    private final RedisIpAttemptService redisIpAttemptService;

    private final BlacklistIpRepository blacklistIpRepository;

    private final AuthenticationConfiguration authenticationConfiguration;
    
    private final CustomUserDetailsService customUserDetailsService;

    private final SlackService slackService;

    @Value("${springdoc.account.id}")
    private String username;

    @Value("${springdoc.account.password}")
    private String password;

    @Value("${springdoc.account.role}")
    private String role;

    @Value("${cors.allowed-origins}")
    private String[] corsAllowedOrigins;

    @Value("${cors.allowed-methods}")
    private String[] corsAllowedMethods;

    @Value("${cors.allowed-headers}")
    private String[] corsAllowedHeaders;

    @Value("${cors.allow-credentials}")
    private boolean corsAllowCredentials;

    @Value("${cors.configuration-path}")
    private String corsConfigurationPath;

    private static final String[] PERMIT_ALL = {
            "/login/**",
            "/static/**",
            "/resources/files/**",
            "/configuration/ui",
            "/configuration/security",
            "/webjars/**",
            "/error",
            "/"
    };

    private static final String[] SWAGGER_PATTERNS = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    private static final String[] PERMIT_ALL_API_ENDPOINTS_GET = {
            "/applications/{applicationId}",
            "/recruitments",
            "/news", "/news/**",
            "/blogs", "/blogs/**",
            "/executives", "/executives/**",
            "/awards", "/awards/**",
            "/activity-group", "/activity-group/**",
            "/work-experiences", "/work-experiences/**",
            "/products", "/products/**",
            "/reviews", "/reviews/**",
            "/activity-photos", "/activity-photos/**"
    };

    private static final String[] PERMIT_ALL_API_ENDPOINTS_POST = {
            "/applications",
            "/members/password-reset-requests",
            "/members/password-reset-verifications",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .cors(cors ->
                        cors.configurationSource(corsConfigurationSource())
                )
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(SWAGGER_PATTERNS).hasRole(role)
                                .requestMatchers(PERMIT_ALL).permitAll()
                                .requestMatchers(HttpMethod.GET, PERMIT_ALL_API_ENDPOINTS_GET).permitAll()
                                .requestMatchers(HttpMethod.POST, PERMIT_ALL_API_ENDPOINTS_POST).permitAll()
                                .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(new CustomBasicAuthenticationFilter(authenticationManager, redisIpAttemptService, blacklistIpRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisTokenService, redisIpAttemptService, slackService, blacklistIpRepository), UsernamePasswordAuthenticationFilter.class);
//                .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
//                        httpSecurityExceptionHandlingConfigurer
//                                .authenticationEntryPoint((request, response, authException) -> {
//                                    String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
//                                    slackService.sendSecurityAlertNotification(request, SecurityAlertType.ABNORMAL_ACCESS);
//                                    log.info("[{}] : 비정상적인 접근이 감지되었습니다.", clientIpAddress);
//                                    redisIpAttemptService.registerLoginAttempt(clientIpAddress);
//                                    ResponseModel responseModel = ResponseModel.builder()
//                                            .success(false)
//                                            .build();
//                                    response.getWriter().write(responseModel.toJson());
//                                    response.setContentType("application/json");
//                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                                })
//                                .accessDeniedHandler((request, response, accessDeniedException) -> {
//                                    String clientIpAddress = HttpReqResUtil.getClientIpAddressIfServletRequestExist();
//                                    slackService.sendSecurityAlertNotification(request, SecurityAlertType.ABNORMAL_ACCESS);
//                                    log.info("[{}] : 비정상적인 접근이 감지되었습니다.", clientIpAddress);
//                                    redisIpAttemptService.registerLoginAttempt(clientIpAddress);
//                                    ResponseModel responseModel = ResponseModel.builder()
//                                            .success(false)
//                                            .build();
//                                    response.getWriter().write(responseModel.toJson());
//                                    response.setContentType("application/json");
//                                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                                })
//                );
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user =
                User.withUsername(username)
                        .password(passwordEncoder().encode(password))
                        .roles(role)
                        .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean(name = "loginAuthenticationManager")
    public AuthenticationManager loginAuthenticationManager() {
        DaoAuthenticationProvider loginProvider = new DaoAuthenticationProvider();
        loginProvider.setUserDetailsService(customUserDetailsService);
        loginProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(Arrays.asList(loginProvider));
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOriginPatterns(
                List.of(corsAllowedOrigins)
        );
        corsConfiguration.setAllowedMethods(List.of(corsAllowedMethods));
        corsConfiguration.setAllowedHeaders(List.of(corsAllowedHeaders));
        corsConfiguration.setAllowCredentials(corsAllowCredentials);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(corsConfigurationPath, corsConfiguration);

        return source;
    }

}