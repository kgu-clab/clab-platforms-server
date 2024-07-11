package page.clab.api.domain.activity.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.activity.domain.ActivityGroup;
import page.clab.api.domain.activity.domain.ApplyForm;

import java.util.List;

public interface ApplyFormRepository extends JpaRepository<ApplyForm, Long> {

    List<ApplyForm> findAllByActivityGroup(ActivityGroup activityGroup);

}
