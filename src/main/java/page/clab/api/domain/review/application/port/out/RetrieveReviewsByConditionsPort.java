package page.clab.api.domain.review.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.review.domain.Review;

public interface RetrieveReviewsByConditionsPort {
    Page<Review> findByConditions(String memberId, String memberName, Long activityId, Boolean isPublic, Pageable pageable);
}
