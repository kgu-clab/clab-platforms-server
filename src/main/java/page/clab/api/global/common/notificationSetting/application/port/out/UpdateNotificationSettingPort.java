package page.clab.api.global.common.notificationSetting.application.port.out;

import page.clab.api.global.common.notificationSetting.domain.NotificationSetting;

public interface UpdateNotificationSettingPort {

    NotificationSetting save(NotificationSetting setting);
}
