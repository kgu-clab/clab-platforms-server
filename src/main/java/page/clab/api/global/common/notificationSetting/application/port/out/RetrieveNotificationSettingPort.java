package page.clab.api.global.common.notificationSetting.application.port.out;

import java.util.List;
import java.util.Optional;
import page.clab.api.global.common.notificationSetting.domain.AlertType;
import page.clab.api.global.common.notificationSetting.domain.NotificationSetting;

public interface RetrieveNotificationSettingPort {

    List<NotificationSetting> findAll();

    Optional<NotificationSetting> findByAlertType(AlertType alertType);
}
