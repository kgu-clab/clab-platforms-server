package page.clab.api.domain.review.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.review.domain.Review;

public interface ReviewRepository extends JpaRepository<ReviewJpaEntity, Long>, ReviewRepositoryCustom, QuerydslPredicateExecutor<Review> {

    boolean existsByMemberIdAndActivityGroup(String memberId, ActivityGroup activityGroup);

    Page<ReviewJpaEntity> findAllByMemberId(String memberId, Pageable pageable);

    @Query(value = "SELECT r.* FROM review r WHERE r.is_deleted = true", nativeQuery = true)
    Page<ReviewJpaEntity> findAllByIsDeletedTrue(Pageable pageable);
}
