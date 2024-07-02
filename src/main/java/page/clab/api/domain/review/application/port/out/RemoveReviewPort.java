package page.clab.api.domain.review.application.port.out;

import page.clab.api.domain.review.domain.Review;

public interface RemoveReviewPort {
    Review findByIdOrThrow(Long reviewId);
    Review save(Review review);
}