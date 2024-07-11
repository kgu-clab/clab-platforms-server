package page.clab.api.domain.activity.review.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.activity.review.application.dto.request.ReviewUpdateRequestDto;
import page.clab.api.domain.activity.review.application.port.in.UpdateReviewUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Activity - Review", description = "활동 리뷰")
public class ReviewUpdateController {

    private final UpdateReviewUseCase updateReviewUseCase;

    @Operation(summary = "[U] 리뷰 수정", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({ "ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER" })
    @PatchMapping("/{reviewId}")
    public ApiResponse<Long> updateReview(
            @PathVariable(name = "reviewId") Long reviewId,
            @Valid @RequestBody ReviewUpdateRequestDto requestDto
    ) throws PermissionDeniedException {
        Long id = updateReviewUseCase.updateReview(reviewId, requestDto);
        return ApiResponse.success(id);
    }
}
