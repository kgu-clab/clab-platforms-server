package page.clab.api.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.GroupSchedule;

@Repository
public interface GroupScheduleRepository extends JpaRepository<GroupSchedule, Long> {

    Page<GroupSchedule> findAllByActivityGroupIdOrderByIdDesc(Long activityGroupId, Pageable pageable);

}
