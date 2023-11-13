package page.clab.api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.GroupSchedule;

@Repository
public interface GroupScheduleRepository extends JpaRepository<GroupSchedule, Long> {

    List<GroupSchedule> findAllByActivityGroupId(Long activityGroupId);

}
