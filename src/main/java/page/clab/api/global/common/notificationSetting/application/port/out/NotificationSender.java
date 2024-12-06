package page.clab.api.global.common.notificationSetting.application.port.out;

import page.clab.api.global.common.notificationSetting.application.event.NotificationEvent;

public interface NotificationSender {

    String getPlatformName();

    void sendNotification(NotificationEvent event, String webhookUrl);
}
