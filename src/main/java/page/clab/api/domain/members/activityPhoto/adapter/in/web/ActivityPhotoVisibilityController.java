package page.clab.api.domain.members.activityPhoto.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.activityPhoto.application.port.in.ToggleActivityPhotoVisibilityUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/activity-photos")
@RequiredArgsConstructor
@Tag(name = "Members - Activity Photo", description = "동아리 활동 사진")
public class ActivityPhotoVisibilityController {

    private final ToggleActivityPhotoVisibilityUseCase toggleActivityPhotoVisibilityUseCase;

    @Operation(summary = "[A] 활동 사진 고정/해제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{activityPhotoId}")
    public ApiResponse<Long> toggleActivityPhotoVisibility(
        @PathVariable(name = "activityPhotoId") Long activityPhotoId
    ) {
        Long id = toggleActivityPhotoVisibilityUseCase.toggleActivityPhotoVisibility(activityPhotoId);
        return ApiResponse.success(id);
    }
}
