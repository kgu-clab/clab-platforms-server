package page.clab.api.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.stream.Stream;

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
