package page.clab.api.global.common.notificationSetting.application.port.out;

import page.clab.api.global.common.notificationSetting.application.event.NotificationEvent;

public interface NotificationSender {

    void sendNotification(NotificationEvent event);
}
