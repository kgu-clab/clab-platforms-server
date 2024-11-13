package page.clab.api.global.common.notificationSetting.application.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import page.clab.api.global.common.notificationSetting.application.port.out.NotificationSender;
import page.clab.api.global.common.notificationSetting.application.service.NotificationSettingService;
import page.clab.api.global.common.notificationSetting.domain.AlertType;
import page.clab.api.global.common.notificationSetting.domain.NotificationSetting;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationSettingService settingService;
    private final NotificationSender notificationSender;

    @EventListener
    public void handleNotificationEvent(NotificationEvent event) {
        AlertType alertType = event.getAlertType();
        NotificationSetting setting = settingService.getOrCreateDefaultSetting(alertType);

        if (setting.isEnabled()) {
            notificationSender.sendNotification(event);
        }
    }
}
