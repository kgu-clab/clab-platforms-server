package page.clab.api.domain.activityGroup.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupReport;

@Repository
public interface ActivityGroupReportRepository extends JpaRepository<ActivityGroupReport, Long> {

    ActivityGroupReport findByActivityGroupAndTurn(ActivityGroup activityGroup, Long turn);

    Page<ActivityGroupReport> findAllByActivityGroup(ActivityGroup activityGroup, Pageable pageable);

}
