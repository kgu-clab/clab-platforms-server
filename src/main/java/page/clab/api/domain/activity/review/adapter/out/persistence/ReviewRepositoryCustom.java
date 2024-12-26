package page.clab.api.domain.activity.review.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {

    Page<ReviewJpaEntity> findByConditions(String memberId, String memberName, Long activityId, Boolean isPublic,
        Pageable pageable);
}
