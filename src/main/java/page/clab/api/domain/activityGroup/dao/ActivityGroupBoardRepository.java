package page.clab.api.domain.activityGroup.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoard;

@Repository
public interface ActivityGroupBoardRepository extends JpaRepository<ActivityGroupBoard, Long> {

    Page<ActivityGroupBoard> findAllByOrderByCreatedAtDesc(Pageable pageable);

    boolean existsById(Long id);

}