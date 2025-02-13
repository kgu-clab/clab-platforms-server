package page.clab.api.domain.activity.review.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.activity.review.application.port.in.RemoveReviewUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Activity - Review", description = "활동 리뷰")
public class ReviewRemoveController {

    private final RemoveReviewUseCase removeReviewUseCase;

    @Operation(summary = "[U] 리뷰 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{reviewId}")
    public ApiResponse<Long> removeReview(
        @PathVariable(name = "reviewId") Long reviewId
    ) {
        Long id = removeReviewUseCase.removeReview(reviewId);
        return ApiResponse.success(id);
    }
}
