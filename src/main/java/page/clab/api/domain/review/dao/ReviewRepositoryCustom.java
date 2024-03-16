package page.clab.api.domain.review.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.review.domain.Review;

public interface ReviewRepositoryCustom {

    Page<Review> findReviewsByConditions(String memberId, String memberName, Long activityId, Boolean isPublic, Pageable pageable);

}
