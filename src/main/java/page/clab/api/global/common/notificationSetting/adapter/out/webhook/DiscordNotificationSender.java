package page.clab.api.global.common.notificationSetting.adapter.out.webhook;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.global.common.notificationSetting.application.event.NotificationEvent;
import page.clab.api.global.common.notificationSetting.application.port.out.NotificationSender;
import page.clab.api.global.common.notificationSetting.domain.PlatformType;

@Component
@RequiredArgsConstructor
public class DiscordNotificationSender implements NotificationSender {

    private final DiscordWebhookClient discordWebhookClient;

    @Override
    public String getPlatformName() {
        return PlatformType.DISCORD.getName();
    }

    @Override
    public void sendNotification(NotificationEvent event, String webhookUrl) {
        discordWebhookClient.sendMessage(webhookUrl, event.getAlertType(), event.getRequest(),
                event.getAdditionalData());
    }
}
