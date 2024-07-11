package page.clab.api.domain.activity.activitygroup.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupReport;

@Repository
public interface ActivityGroupReportRepository extends JpaRepository<ActivityGroupReport, Long> {

    ActivityGroupReport findByActivityGroupAndTurn(ActivityGroup activityGroup, Long turn);

    Page<ActivityGroupReport> findAllByActivityGroup(ActivityGroup activityGroup, Pageable pageable);

    boolean existsByActivityGroupAndTurn(ActivityGroup activityGroup, Long turn);

    @Query(value = "SELECT a.* FROM activity_group_report a WHERE a.is_deleted = true", nativeQuery = true)
    Page<ActivityGroupReport> findAllByIsDeletedTrue(Pageable pageable);

}
