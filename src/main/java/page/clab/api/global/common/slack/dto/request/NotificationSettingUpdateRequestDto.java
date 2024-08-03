package page.clab.api.global.common.slack.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationSettingUpdateRequestDto {

    @NotNull(message = "{notNull.notificationSetting.alertType}")
    @Schema(description = "알림 타입", example = "서버 시작")
    private String alertType;

    @NotNull(message = "{notNull.notificationSetting.enabled}")
    @Schema(description = "알림 활성화 여부", example = "true")
    private boolean enabled;
}
