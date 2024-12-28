package page.clab.api.domain.activity.review.application.port.out;

import page.clab.api.domain.activity.review.domain.Review;

public interface RegisterReviewPort {

    Review save(Review review);
}
