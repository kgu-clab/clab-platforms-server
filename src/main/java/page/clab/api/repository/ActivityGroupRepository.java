package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.ActivityGroup;
import page.clab.api.type.etc.ActivityGroupCategory;
import page.clab.api.type.etc.ActivityGroupStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityGroupRepository extends JpaRepository<ActivityGroup, Long> {

    Optional<ActivityGroup> findById(Long id);

    List<ActivityGroup> findAllByCategory(ActivityGroupCategory category);

    List<ActivityGroup> findAllByStatus(ActivityGroupStatus status);

    Optional<ActivityGroup> findByIdAndCategory(Long id, ActivityGroupStatus category);
    
}
