package page.clab.api.domain.review.application.port.in;

import page.clab.api.global.exception.PermissionDeniedException;

public interface RemoveReviewUseCase {
    Long removeReview(Long reviewId) throws PermissionDeniedException;
}
