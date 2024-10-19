package page.clab.api.global.common.slack.dto.mapper;

import org.springframework.stereotype.Component;
import page.clab.api.global.common.slack.domain.NotificationSetting;
import page.clab.api.global.common.slack.dto.response.NotificationSettingResponseDto;

@Component
public class SlackDtoMapper {

    public NotificationSettingResponseDto toDto(NotificationSetting setting) {
        return NotificationSettingResponseDto.builder()
                .alertType(setting.getAlertType().getTitle())
                .enabled(setting.isEnabled())
                .build();
    }
}
