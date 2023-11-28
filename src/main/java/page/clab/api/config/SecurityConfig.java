package page.clab.api.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import page.clab.api.auth.filter.JwtAuthenticationFilter;
import page.clab.api.auth.jwt.JwtTokenProvider;
import page.clab.api.repository.BlacklistIpRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    private final BlacklistIpRepository blacklistIpRepository;

    @Value("${springdoc.account.id}")
    private String username;

    @Value("${springdoc.account.password}")
    private String password;

    private static final String[] PERMIT_ALL = {
            "/login/**",
            "/static/**",
            "/configuration/ui",
            "/configuration/security",
            "/webjars/**",
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

    private static final String[] PERMIT_ALL_API_ENDPOINTS = {
            "/applications/{applicationId}",
            "/recruitments",
            "/news",
            "/news/**",
            "/blogs",
            "/blogs/**",
            "/executives",
            "/executives/**",
            "/awards",
            "/awards/**",
            "/activity-group",
            "/activity-group/**",
            "/work-experiences",
            "/work-experiences/**",
            "/products",
            "/products/**",
            "/reviews",
            "/reviews/**",
            "/activity-photos",
            "/activity-photos/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(SWAGGER_PATTERNS).hasRole("SWAGGER")
                .antMatchers(PERMIT_ALL).permitAll()
                .antMatchers(HttpMethod.POST, "/applications").permitAll()
                .antMatchers("/applications/filter", "/applications/pass", "/applications/search").hasAnyAuthority("ADMIN", "SUPER")
                .antMatchers(HttpMethod.GET, PERMIT_ALL_API_ENDPOINTS).permitAll()
                .antMatchers("/**").hasAnyAuthority("USER", "ADMIN", "SUPER")
                .and()
                .httpBasic().realmName("Swagger") // Set realm name for Basic Auth
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, blacklistIpRepository), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
    UserDetails user =
        User.withUsername(username)
            .password(passwordEncoder().encode(password))
            .roles("SWAGGER")
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOriginPatterns(
                List.of(
                        "http://clab.page", "https://clab.page",
                        "http://*.clab.page", "https://*.clab.page",
                        "https://localhost:6001"
                )
        );
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

}