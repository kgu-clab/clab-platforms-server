package page.clab.api.global.common.notificationSetting.adapter.out.slack;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.global.common.notificationSetting.application.event.NotificationEvent;
import page.clab.api.global.common.notificationSetting.application.port.out.NotificationSender;

@Component
@RequiredArgsConstructor
public class SlackNotificationSender implements NotificationSender {

    private final SlackServiceHelper slackServiceHelper;

    @Override
    public void sendNotification(NotificationEvent event) {
        slackServiceHelper.sendSlackMessage(event.getWebhookUrl(), event.getAlertType(), event.getRequest(),
                event.getAdditionalData());
    }
}
