package page.clab.api.global.common.notificationSetting.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.notificationSetting.application.dto.response.NotificationSettingResponseDto;
import page.clab.api.global.common.notificationSetting.application.port.in.RetrieveNotificationSettingUseCase;

@RestController
@RequestMapping("/api/v1/notification-settings")
@RequiredArgsConstructor
@Tag(name = "Notification Setting", description = "웹훅 알림 설정")
public class NotificationSettingRetrieveController {

    private final RetrieveNotificationSettingUseCase retrieveNotificationSettingUseCase;

    @Operation(summary = "[S] 웹훅 알림 조회", description = "ROLE_SUPER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('SUPER')")
    @GetMapping("")
    public ApiResponse<List<NotificationSettingResponseDto>> getNotificationSettings() {
        List<NotificationSettingResponseDto> notificationSettings = retrieveNotificationSettingUseCase.retrieveNotificationSettings();
        return ApiResponse.success(notificationSettings);
    }
}
