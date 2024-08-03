package page.clab.api.global.common.slack.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.global.common.slack.domain.NotificationSetting;

@Getter
@Builder
public class NotificationSettingResponseDto {

    private String alertType;
    private boolean enabled;

    public static NotificationSettingResponseDto toDto(NotificationSetting setting) {
        return NotificationSettingResponseDto.builder()
                .alertType(setting.getAlertType().getTitle())
                .enabled(setting.isEnabled())
                .build();
    }
}
