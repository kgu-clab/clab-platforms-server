package page.clab.api.domain.review.application.port.in;

import page.clab.api.domain.review.domain.Review;

public interface ReviewLookupUseCase {
    Review getReviewByIdOrThrow(Long reviewId);
}
