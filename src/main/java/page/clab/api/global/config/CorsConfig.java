package page.clab.api.global.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Value("${security.cors.allowed-origins}")
    private String[] corsAllowedOrigins;

    @Value("${security.cors.allowed-methods}")
    private String[] corsAllowedMethods;

    @Value("${security.cors.allowed-headers}")
    private String[] corsAllowedHeaders;

    @Value("${security.cors.allow-credentials}")
    private boolean corsAllowCredentials;

    @Value("${security.cors.configuration-path}")
    private String corsConfigurationPath;

    @Value("${security.auth.header}")
    private String authHeader;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOriginPatterns(
            List.of(corsAllowedOrigins)
        );
        corsConfiguration.setAllowedMethods(List.of(corsAllowedMethods));
        corsConfiguration.setAllowedHeaders(List.of(corsAllowedHeaders));
        corsConfiguration.setAllowCredentials(corsAllowCredentials);
        corsConfiguration.addExposedHeader(authHeader);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(corsConfigurationPath, corsConfiguration);

        return source;
    }
}
