package page.clab.api.global.common.notificationSetting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import page.clab.api.global.common.notificationSetting.adapter.out.slack.SlackNotificationSender;
import page.clab.api.global.common.notificationSetting.application.port.out.NotificationSender;

@Configuration
public class NotificationConfig {

    private final SlackNotificationSender slackNotificationSender;

    public NotificationConfig(SlackNotificationSender slackNotificationSender) {
        this.slackNotificationSender = slackNotificationSender;
    }

    @Bean
    public NotificationSender notificationSender() {
        return slackNotificationSender;
    }
}
