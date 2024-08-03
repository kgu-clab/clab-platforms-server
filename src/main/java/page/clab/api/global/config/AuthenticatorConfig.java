package page.clab.api.global.config;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticatorConfig {

    @Bean
    public GoogleAuthenticator googleAuthenticator() {
        return new GoogleAuthenticator();
    }
}
