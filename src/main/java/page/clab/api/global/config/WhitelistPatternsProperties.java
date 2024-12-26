package page.clab.api.global.config;

import java.util.Arrays;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "security.whitelist.patterns")
public class WhitelistPatternsProperties {

    private String[] actuator;
    private String[] apiDocs;

    public String[] getWhitelistPatterns() {
        return Stream.of(apiDocs, actuator)
            .flatMap(Arrays::stream)
            .toArray(String[]::new);
    }
}
