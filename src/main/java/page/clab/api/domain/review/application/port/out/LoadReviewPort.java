package page.clab.api.domain.review.application.port.out;

import page.clab.api.domain.review.domain.Review;

import java.util.Optional;

public interface LoadReviewPort {
    Optional<Review> findById(Long reviewId);
    Review findByIdOrThrow(Long reviewId);
}
