package page.clab.api.domain.activity.review.application.port.out;

import page.clab.api.domain.activity.review.domain.Review;

public interface UpdateReviewPort {
    Review update(Review review);
}
