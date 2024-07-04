package page.clab.api.domain.activityPhoto.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.activityPhoto.application.port.in.ToggleActivityPhotoVisibilityUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/activity-photos")
@RequiredArgsConstructor
@Tag(name = "ActivityPhoto", description = "활동 사진")
public class ActivityPhotoVisibilityController {

    private final ToggleActivityPhotoVisibilityUseCase toggleActivityPhotoVisibilityUseCase;

    @Operation(summary = "활동 사진 고정/해제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({ "ROLE_ADMIN", "ROLE_SUPER" })
    @PatchMapping("/{activityPhotoId}")
    public ApiResponse<Long> toggleActivityPhotoVisibility(
            @PathVariable(name = "activityPhotoId") Long activityPhotoId
    ) {
        Long id = toggleActivityPhotoVisibilityUseCase.toggleActivityPhotoVisibility(activityPhotoId);
        return ApiResponse.success(id);
    }
}
