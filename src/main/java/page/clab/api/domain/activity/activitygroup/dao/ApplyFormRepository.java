package page.clab.api.domain.activity.activitygroup.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ApplyForm;

public interface ApplyFormRepository extends JpaRepository<ApplyForm, Long> {

    List<ApplyForm> findAllByActivityGroup(ActivityGroup activityGroup);
}
