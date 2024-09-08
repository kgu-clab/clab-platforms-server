package page.clab.api.domain.activity.activitygroup.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupStatus;

public interface ActivityGroupRepositoryCustom {

    Page<ActivityGroup> findActivityGroupsByStatus(ActivityGroupStatus status, Pageable pageable);
}
