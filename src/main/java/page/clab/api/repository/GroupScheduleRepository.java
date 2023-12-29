package page.clab.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.GroupSchedule;

import java.util.List;

@Repository
public interface GroupScheduleRepository extends JpaRepository<GroupSchedule, Long> {

    List<GroupSchedule> findAllByActivityGroupIdOrderByIdDesc(Long activityGroupId);
    
    Page<GroupSchedule> findAllByActivityGroupIdOrderByIdDesc(Long activityGroupId, Pageable pageable);

}
