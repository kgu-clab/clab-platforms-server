package page.clab.api.domain.review.application;

import page.clab.api.domain.review.domain.Review;

public interface ReviewLookupService {
    Review getReviewByIdOrThrow(Long reviewId);
}
