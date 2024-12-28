package page.clab.api.global.common.notificationSetting.config;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "notification")
@Getter
@Setter
public class NotificationConfigProperties {

    private CommonProperties common;
    private Map<String, PlatformConfig> platforms;
    private Map<String, List<PlatformMapping>> categoryMappings;
    private List<PlatformMapping> defaultMappings;

    @Getter
    @Setter
    public static class CommonProperties {

        private String webUrl;
        private String apiUrl;
        private String color;

        public int getColorAsInt() {
            return Integer.parseInt(color.replaceFirst("^#", ""), 16);
        }
    }

    @Getter
    @Setter
    public static class PlatformConfig {

        private Map<String, String> webhooks;
    }

    @Getter
    @Setter
    public static class PlatformMapping {

        private String platform;
        private String webhook;
    }
}
