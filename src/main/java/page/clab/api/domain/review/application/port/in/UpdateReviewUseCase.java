package page.clab.api.domain.review.application.port.in;

import page.clab.api.domain.review.application.dto.request.ReviewUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface UpdateReviewUseCase {
    Long updateReview(Long reviewId, ReviewUpdateRequestDto requestDto) throws PermissionDeniedException;
}
