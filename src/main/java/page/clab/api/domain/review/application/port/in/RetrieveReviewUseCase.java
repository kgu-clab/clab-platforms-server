package page.clab.api.domain.review.application.port.in;

import page.clab.api.domain.review.domain.Review;

public interface RetrieveReviewUseCase {
    Review findByIdOrThrow(Long reviewId);
}
