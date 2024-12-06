package page.clab.api.global.common.slack.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import page.clab.api.global.common.slack.application.NotificationSettingService;
import page.clab.api.global.common.slack.application.SlackServiceHelper;
import page.clab.api.global.common.slack.domain.AlertType;
import page.clab.api.global.common.slack.domain.NotificationSetting;
import page.clab.api.global.common.slack.event.NotificationEvent;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationSettingService settingService;
    private final SlackServiceHelper slackServiceHelper;

    @EventListener
    public void handleNotificationEvent(NotificationEvent event) {
        AlertType alertType = event.getAlertType();
        NotificationSetting setting = settingService.getOrCreateDefaultSetting(alertType);

        if (setting.isEnabled()) {
            slackServiceHelper.sendSlackMessage(event.getWebhookUrl(), alertType, event.getRequest(), event.getAdditionalData());
        }
    }
}
