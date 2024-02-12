package page.clab.api.domain.activityGroup.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ApplyForm;

public interface ApplyFormRepository extends JpaRepository<ApplyForm, Long> {

    Page<ApplyForm> findAllByActivityGroup(ActivityGroup activityGroup, Pageable pageable);

}
