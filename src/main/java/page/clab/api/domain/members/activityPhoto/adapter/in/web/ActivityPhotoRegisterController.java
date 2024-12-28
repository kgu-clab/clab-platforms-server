package page.clab.api.domain.members.activityPhoto.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.activityPhoto.application.dto.request.ActivityPhotoRequestDto;
import page.clab.api.domain.members.activityPhoto.application.port.in.RegisterActivityPhotoUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/activity-photos")
@RequiredArgsConstructor
@Tag(name = "Members - Activity Photo", description = "동아리 활동 사진")
public class ActivityPhotoRegisterController {

    private final RegisterActivityPhotoUseCase registerActivityPhotoUseCase;

    @Operation(summary = "[A] 활동 사진 등록", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ApiResponse<Long> registerActivityPhoto(
        @Valid @RequestBody ActivityPhotoRequestDto requestDto
    ) {
        Long id = registerActivityPhotoUseCase.registerActivityPhoto(requestDto);
        return ApiResponse.success(id);
    }
}
