package page.clab.api.domain.activityGroup.dao;

import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupStatus;

import java.util.List;

public interface ActivityGroupRepositoryCustom {

    List<ActivityGroup> findActivityGroupsByStatus(ActivityGroupStatus status);

}
