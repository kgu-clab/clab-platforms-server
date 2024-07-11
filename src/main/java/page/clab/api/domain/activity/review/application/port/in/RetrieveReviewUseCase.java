package page.clab.api.domain.activity.review.application.port.in;

import page.clab.api.domain.activity.review.domain.Review;

public interface RetrieveReviewUseCase {
    Review findByIdOrThrow(Long reviewId);
}
