package page.clab.api.global.common.notificationSetting.application.port.in;

import java.util.List;
import page.clab.api.global.common.notificationSetting.application.dto.response.NotificationSettingResponseDto;

public interface RetrieveNotificationSettingUseCase {

    List<NotificationSettingResponseDto> retrieveNotificationSettings();
}
