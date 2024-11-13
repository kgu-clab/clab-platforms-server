package page.clab.api.global.common.notificationSetting.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.notificationSetting.application.dto.request.NotificationSettingUpdateRequestDto;
import page.clab.api.global.common.notificationSetting.application.dto.response.NotificationSettingResponseDto;
import page.clab.api.global.common.notificationSetting.application.service.NotificationSettingService;

@RestController
@RequestMapping("/api/v1/notification-settings")
@RequiredArgsConstructor
@Tag(name = "Notification Setting", description = "알림 설정")
public class NotificationSettingController {

    private final NotificationSettingService notificationSettingService;

    @Operation(summary = "[S] 슬랙 알림 조회", description = "ROLE_SUPER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('SUPER')")
    @GetMapping("")
    public ApiResponse<List<NotificationSettingResponseDto>> getNotificationSettings() {
        List<NotificationSettingResponseDto> notificationSettings = notificationSettingService.getNotificationSettings();
        return ApiResponse.success(notificationSettings);
    }

    @Operation(summary = "[S] 슬랙 알림 설정 변경", description = "ROLE_SUPER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('SUPER')")
    @PutMapping("")
    public ApiResponse<Void> updateNotificationSetting(
            @Valid @RequestBody NotificationSettingUpdateRequestDto requestDto
    ) {
        notificationSettingService.updateNotificationSetting(requestDto.getAlertType(), requestDto.isEnabled());
        return ApiResponse.success();
    }
}
