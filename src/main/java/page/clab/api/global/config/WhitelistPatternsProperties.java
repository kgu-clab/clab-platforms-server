package page.clab.api.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Map;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "security.whitelist")
public class WhitelistPatternsProperties {

    private Map<String, String[]> patterns;

    public String[] getWhitelistPatterns() {
        return patterns.values().stream()
                .flatMap(Arrays::stream)
                .toArray(String[]::new);
    }
}
