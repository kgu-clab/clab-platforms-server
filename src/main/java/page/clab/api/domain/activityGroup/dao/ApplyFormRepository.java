package page.clab.api.domain.activityGroup.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ApplyForm;

import java.util.List;

public interface ApplyFormRepository extends JpaRepository<ApplyForm, Long> {

    List<ApplyForm> findAllByActivityGroup(ActivityGroup activityGroup);

}
