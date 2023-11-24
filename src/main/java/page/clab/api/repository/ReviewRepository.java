package page.clab.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.type.entity.ActivityGroup;
import page.clab.api.type.entity.Member;
import page.clab.api.type.entity.Review;
import page.clab.api.type.etc.ActivityGroupCategory;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByMemberAndActivityGroup(Member member, ActivityGroup activityGroup);

    Page<Review> findAllByMemberOrderByCreatedAtDesc(Member member, Pageable pageable);

    Page<Review> findAllByIsPublicOrderByCreatedAtDesc(boolean isPublic, Pageable pageable);

    Page<Review> findAllByMember_IdOrderByCreatedAtDesc(String memberId, Pageable pageable);

    Page<Review> findAllByMember_NameOrderByCreatedAtDesc(String name, Pageable pageable);

    Page<Review> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Review> findAllByActivityGroup_IdOrderByCreatedAtDesc(Long activityGroupId, Pageable pageable);

    Page<Review> findAllByActivityGroup_CategoryOrderByCreatedAtDesc(ActivityGroupCategory activityGroupCategory, Pageable pageable);

}
