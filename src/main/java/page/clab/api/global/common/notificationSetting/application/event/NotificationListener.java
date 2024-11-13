package page.clab.api.global.common.notificationSetting.application.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import page.clab.api.global.common.notificationSetting.application.port.in.UpdateNotificationSettingUseCase;
import page.clab.api.global.common.notificationSetting.application.port.out.NotificationSender;
import page.clab.api.global.common.notificationSetting.domain.AlertType;
import page.clab.api.global.common.notificationSetting.domain.NotificationSetting;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final UpdateNotificationSettingUseCase updateNotificationSettingUseCase;
    private final NotificationSender notificationSender;

    @EventListener
    public void handleNotificationEvent(NotificationEvent event) {
        AlertType alertType = event.getAlertType();
        NotificationSetting setting = updateNotificationSettingUseCase.getOrCreateDefaultSetting(alertType);

        if (setting.isEnabled()) {
            notificationSender.sendNotification(event);
        }
    }
}
