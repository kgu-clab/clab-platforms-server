package page.clab.api.domain.review.application.port.out;

import page.clab.api.domain.review.domain.Review;

public interface RetrieveReviewByIdPort {
    Review findByIdOrThrow(Long reviewId);
}
