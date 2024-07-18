package page.clab.api.domain.activity.activitygroup.dao;

import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupStatus;

import java.util.List;

public interface ActivityGroupRepositoryCustom {
    List<ActivityGroup> findActivityGroupsByStatus(ActivityGroupStatus status);
}
