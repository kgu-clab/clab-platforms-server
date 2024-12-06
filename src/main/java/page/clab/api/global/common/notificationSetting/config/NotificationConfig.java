package page.clab.api.global.common.notificationSetting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfig {

    @Bean
    public NotificationConfigProperties notificationConfigProperties() {
        return new NotificationConfigProperties();
    }
}
