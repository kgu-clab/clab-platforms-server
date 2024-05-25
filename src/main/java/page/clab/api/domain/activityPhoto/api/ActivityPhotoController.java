package page.clab.api.domain.activityPhoto.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.activityPhoto.application.ActivityPhotoService;
import page.clab.api.domain.activityPhoto.domain.ActivityPhoto;
import page.clab.api.domain.activityPhoto.dto.request.ActivityPhotoRequestDto;
import page.clab.api.domain.activityPhoto.dto.response.ActivityPhotoResponseDto;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

@RestController
@RequestMapping("/api/v1/activity-photos")
@RequiredArgsConstructor
@Tag(name = "ActivityPhoto", description = "활동 사진")
@Slf4j
public class ActivityPhotoController {

    private final ActivityPhotoService activityPhotoService;

    @Operation(summary = "[A] 활동 사진 등록", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> createActivityPhoto(
            @Valid @RequestBody ActivityPhotoRequestDto requestDto
    ) {
        Long id = activityPhotoService.createActivityPhoto(requestDto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "활동 사진 목록 조회", description = "ROLE_ANONYMOUS 이상의 권한이 필요함<br> " +
            "공개 여부를 입력하지 않으면 전체 조회됨")
    @GetMapping("")
    public ApiResponse<PagedResponseDto<ActivityPhotoResponseDto>> getActivityPhotosByConditions(
            @RequestParam(name = "isPublic", required = false) Boolean isPublic,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", required = false) Optional<List<String>> sortBy,
            @RequestParam(name = "sortDirection", required = false) Optional<List<String>> sortDirection
    ) throws SortingArgumentException {
        List<String> sortByList = sortBy.orElse(List.of("createdAt"));
        List<String> sortDirectionList = sortDirection.orElse(List.of("desc"));
        Pageable pageable = PageableUtils.createPageable(page, size, sortByList, sortDirectionList, ActivityPhoto.class);
        PagedResponseDto<ActivityPhotoResponseDto> activityPhotos = activityPhotoService.getActivityPhotosByConditions(isPublic, pageable);
        return ApiResponse.success(activityPhotos);
    }

    @Operation(summary = "활동 사진 고정/해제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{activityPhotoId}")
    public ApiResponse<Long> togglePublicStatus(
            @PathVariable(name = "activityPhotoId") Long activityPhotoId
    ) {
        Long id = activityPhotoService.togglePublicStatus(activityPhotoId);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[A] 활동 사진 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{activityPhotoId}")
    public ApiResponse<Long> deleteActivityPhoto(
            @PathVariable(name = "activityPhotoId") Long activityPhotoId
    ) {
        Long id = activityPhotoService.deleteActivityPhoto(activityPhotoId);
        return ApiResponse.success(id);
    }

}
