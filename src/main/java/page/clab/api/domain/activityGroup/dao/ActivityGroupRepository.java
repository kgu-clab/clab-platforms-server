package page.clab.api.domain.activityGroup.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupCategory;
import page.clab.api.domain.activityGroup.domain.ActivityGroupStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityGroupRepository extends JpaRepository<ActivityGroup, Long> {

    Optional<ActivityGroup> findById(Long id);

    Page<ActivityGroup> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<ActivityGroup> findAllByCategoryOrderByCreatedAtDesc(ActivityGroupCategory category, Pageable pageable);

    List<ActivityGroup> findAllByStatusOrderByCreatedAtDesc(ActivityGroupStatus status);

}
