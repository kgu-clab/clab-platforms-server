package page.clab.api.domain.review.application.port.out;

import page.clab.api.domain.review.domain.Review;

public interface RegisterReviewPort {
    Review save(Review review);
}
