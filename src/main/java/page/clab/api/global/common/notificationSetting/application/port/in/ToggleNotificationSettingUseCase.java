package page.clab.api.global.common.notificationSetting.application.port.in;

import page.clab.api.global.common.notificationSetting.domain.AlertType;
import page.clab.api.global.common.notificationSetting.domain.NotificationSetting;

public interface ToggleNotificationSettingUseCase {

    void toggleNotificationSetting(String alertTypeName, boolean enabled);

    NotificationSetting getOrCreateDefaultSetting(AlertType alertType);
}
