package page.clab.api.global.common.notificationSetting.application.dto.mapper;

import org.springframework.stereotype.Component;
import page.clab.api.global.common.notificationSetting.application.dto.response.NotificationSettingResponseDto;
import page.clab.api.global.common.notificationSetting.domain.NotificationSetting;

@Component
public class NotificationSettingDtoMapper {

    public NotificationSettingResponseDto toDto(NotificationSetting setting) {
        return NotificationSettingResponseDto.builder()
                .alertType(setting.getAlertType().getTitle())
                .enabled(setting.isEnabled())
                .build();
    }
}
