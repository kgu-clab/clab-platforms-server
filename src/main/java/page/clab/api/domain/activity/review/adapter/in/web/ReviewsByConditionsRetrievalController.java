package page.clab.api.domain.activity.review.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.activity.review.application.dto.response.ReviewResponseDto;
import page.clab.api.domain.activity.review.application.port.in.RetrieveReviewsByConditionsUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Activity - Review", description = "활동 리뷰")
public class ReviewsByConditionsRetrievalController {

    private final RetrieveReviewsByConditionsUseCase retrieveReviewsByConditionsUseCase;
    private final PageableUtils pageableUtils;

    @Operation(summary = "[U] 리뷰 목록 조회(멤버 ID, 멤버 이름, 활동 ID, 공개 여부 기준)", description = "ROLE_USER 이상의 권한이 필요함<br>" +
        "4개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
        "멤버 ID, 멤버 이름, 활동 ID, 공개 여부 중 하나라도 입력하지 않으면 전체 조회됨<br>" +
        "DTO의 필드명을 기준으로 정렬 가능하며, 정렬 방향은 오름차순(asc)과 내림차순(desc)이 가능함")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("")
    public ApiResponse<PagedResponseDto<ReviewResponseDto>> retrieveReviewsByConditions(
        @RequestParam(name = "memberId", required = false) String memberId,
        @RequestParam(name = "memberName", required = false) String memberName,
        @RequestParam(name = "activityId", required = false) Long activityId,
        @RequestParam(name = "isPublic", required = false) Boolean isPublic,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "20") int size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") List<String> sortBy,
        @RequestParam(name = "sortDirection", defaultValue = "desc") List<String> sortDirection
    ) throws SortingArgumentException {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy, sortDirection, ReviewResponseDto.class);
        PagedResponseDto<ReviewResponseDto> reviews = retrieveReviewsByConditionsUseCase.retrieveReviews(memberId,
            memberName, activityId, isPublic, pageable);
        return ApiResponse.success(reviews);
    }
}
