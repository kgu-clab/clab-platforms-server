package page.clab.api.domain.review.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.review.domain.Review;

public interface ReviewRepositoryCustom {
    Page<Review> findByConditions(String memberId, String memberName, Long activityId, Boolean isPublic, Pageable pageable);
}
