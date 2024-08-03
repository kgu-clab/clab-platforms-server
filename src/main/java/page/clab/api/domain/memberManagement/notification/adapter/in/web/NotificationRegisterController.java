package page.clab.api.domain.memberManagement.notification.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.memberManagement.notification.application.dto.request.NotificationRequestDto;
import page.clab.api.domain.memberManagement.notification.application.port.in.RegisterNotificationUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Member Management - Notification", description = "알림")
public class NotificationRegisterController {

    private final RegisterNotificationUseCase registerNotificationUseCase;

    @Operation(summary = "[U] 알림 생성", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({ "ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER" })
    @PostMapping("")
    public ApiResponse<Long> registerNotification(
            @Valid @RequestBody NotificationRequestDto requestDto
    ) {
        Long id = registerNotificationUseCase.registerNotification(requestDto);
        return ApiResponse.success(id);
    }
}
