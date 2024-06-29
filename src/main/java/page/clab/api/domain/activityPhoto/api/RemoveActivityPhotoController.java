package page.clab.api.domain.activityPhoto.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.activityPhoto.application.RemoveActivityPhotoService;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/activity-photos")
@RequiredArgsConstructor
@Tag(name = "ActivityPhoto", description = "활동 사진")
public class RemoveActivityPhotoController {

    private final RemoveActivityPhotoService removeActivityPhotoService;

    @Operation(summary = "[A] 활동 사진 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{activityPhotoId}")
    public ApiResponse<Long> removeActivityPhoto(
            @PathVariable(name = "activityPhotoId") Long activityPhotoId
    ) {
        Long id = removeActivityPhotoService.removePhoto(activityPhotoId);
        return ApiResponse.success(id);
    }
}