package page.clab.api.global.config;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

@Configuration
@RequiredArgsConstructor
public class AuthorizeRequestsCustomizer {

    private final WhitelistAccountProperties whitelistAccountProperties;
    private final WhitelistPatternsProperties whitelistPatternsProperties;

    @Bean
    public @NotNull Customizer<
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry
        > authorizeHttpRequestsConfig() {
        return authorize -> authorize
            .requestMatchers(SecurityConstants.PERMIT_ALL).permitAll()
            .requestMatchers(HttpMethod.GET, SecurityConstants.PERMIT_ALL_API_ENDPOINTS_GET).permitAll()
            .requestMatchers(HttpMethod.POST, SecurityConstants.PERMIT_ALL_API_ENDPOINTS_POST).permitAll()
            .requestMatchers(whitelistPatternsProperties.getWhitelistPatterns())
            .hasRole(whitelistAccountProperties.getRole())
            .anyRequest().authenticated();
    }
}
