package page.clab.api.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security.jwt.refresh-token.cookie")
public class RefreshTokenCookieProperties {

    private String name;
    private String path;
    private boolean httpOnly;
    private boolean secure;
    private int maxAge;
    private String sameSite;
}
