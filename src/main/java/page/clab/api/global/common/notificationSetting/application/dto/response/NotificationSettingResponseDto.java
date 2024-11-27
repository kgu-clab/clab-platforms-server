package page.clab.api.global.common.notificationSetting.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationSettingResponseDto {

    private String alertType;
    private boolean enabled;
}
