package page.clab.api.domain.review.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.review.application.port.in.RemoveReviewUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Review", description = "리뷰")
public class ReviewRemoveController {

    private final RemoveReviewUseCase removeReviewUseCase;

    @Operation(summary = "[U] 리뷰 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("/{reviewId}")
    public ApiResponse<Long> removeReview(
            @PathVariable(name = "reviewId") Long reviewId
    ) throws PermissionDeniedException {
        Long id = removeReviewUseCase.removeReview(reviewId);
        return ApiResponse.success(id);
    }
}
