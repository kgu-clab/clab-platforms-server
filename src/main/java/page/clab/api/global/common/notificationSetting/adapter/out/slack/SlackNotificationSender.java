package page.clab.api.global.common.notificationSetting.adapter.out.slack;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.global.common.notificationSetting.application.event.NotificationEvent;
import page.clab.api.global.common.notificationSetting.application.port.out.NotificationSender;
import page.clab.api.global.common.notificationSetting.domain.PlatformType;

@Component
@RequiredArgsConstructor
public class SlackNotificationSender implements NotificationSender {

    private final SlackServiceHelper slackServiceHelper;

    @Override
    public String getPlatformName() {
        return PlatformType.SLACK.getName();
    }

    @Override
    public void sendNotification(NotificationEvent event, String webhookUrl) {
        slackServiceHelper.sendSlackMessage(webhookUrl, event.getAlertType(), event.getRequest(),
                event.getAdditionalData());
    }
}
