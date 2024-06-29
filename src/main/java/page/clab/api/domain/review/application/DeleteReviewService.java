package page.clab.api.domain.review.application;

import page.clab.api.global.exception.PermissionDeniedException;

public interface DeleteReviewService {
    Long execute(Long reviewId) throws PermissionDeniedException;
}
