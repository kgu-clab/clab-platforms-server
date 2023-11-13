package page.clab.api.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.ActivityGroup;
import page.clab.api.type.etc.ActivityGroupStatus;

@Repository
public interface ActivityGroupRepository extends JpaRepository<ActivityGroup, Long> {

    Optional<ActivityGroup> findById(Long id);

    List<ActivityGroup> findAllByCategory(String category);

    List<ActivityGroup> findAllByStatus(ActivityGroupStatus status);

    Optional<ActivityGroup> findByIdAndCategory(Long id, String category);
    
}
