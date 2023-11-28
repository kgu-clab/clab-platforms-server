package page.clab.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.ActivityGroup;
import page.clab.api.type.etc.ActivityGroupCategory;
import page.clab.api.type.etc.ActivityGroupStatus;

import java.util.Optional;

@Repository
public interface ActivityGroupRepository extends JpaRepository<ActivityGroup, Long> {

    Optional<ActivityGroup> findById(Long id);

    Page<ActivityGroup> findAllByCategory(ActivityGroupCategory category, Pageable pageable);

    Page<ActivityGroup> findAllByStatusOrderByCreatedAtDesc(ActivityGroupStatus status, Pageable pageable);

    Optional<ActivityGroup> findByIdAndCategory(Long id, String category);
    
}
