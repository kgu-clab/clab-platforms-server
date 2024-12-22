package page.clab.api.domain.members.activityPhoto.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.members.activityPhoto.application.dto.response.ActivityPhotoResponseDto;
import page.clab.api.domain.members.activityPhoto.application.port.in.RetrieveActivityPhotoUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

@RestController
@RequestMapping("/api/v1/activity-photos")
@RequiredArgsConstructor
@Tag(name = "Members - Activity Photo", description = "동아리 활동 사진")
public class ActivityPhotoRetrievalController {

    private final RetrieveActivityPhotoUseCase retrieveActivityPhotoUseCase;
    private final PageableUtils pageableUtils;

    @Operation(summary = "활동 사진 목록 조회", description = "ROLE_ANONYMOUS 이상의 권한이 필요함<br> " +
        "공개 여부를 입력하지 않으면 전체 조회됨<br>" +
        "DTO의 필드명을 기준으로 정렬 가능하며, 정렬 방향은 오름차순(asc)과 내림차순(desc)이 가능함")
    @GetMapping("")
    public ApiResponse<PagedResponseDto<ActivityPhotoResponseDto>> retrieveActivityPhotos(
        @RequestParam(name = "isPublic", required = false) Boolean isPublic,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "20") int size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") List<String> sortBy,
        @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection,
            ActivityPhotoResponseDto.class);
        PagedResponseDto<ActivityPhotoResponseDto> activityPhotos = retrieveActivityPhotoUseCase.retrieveActivityPhotos(
            isPublic, pageable);
        return ApiResponse.success(activityPhotos);
    }
}
