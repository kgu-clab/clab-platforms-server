package page.clab.api.domain.activityPhoto.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.activityPhoto.application.port.in.RegisterActivityPhotoUseCase;
import page.clab.api.domain.activityPhoto.dto.request.ActivityPhotoRequestDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/activity-photos")
@RequiredArgsConstructor
@Tag(name = "ActivityPhoto", description = "활동 사진")
public class ActivityPhotoRegisterController {

    private final RegisterActivityPhotoUseCase registerActivityPhotoUseCase;

    @Operation(summary = "[A] 활동 사진 등록", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> registerActivityPhoto(
            @Valid @RequestBody ActivityPhotoRequestDto requestDto
    ) {
        Long id = registerActivityPhotoUseCase.register(requestDto);
        return ApiResponse.success(id);
    }
}
