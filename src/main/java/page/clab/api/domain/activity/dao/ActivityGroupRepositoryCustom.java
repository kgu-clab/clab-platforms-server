package page.clab.api.domain.activity.dao;

import page.clab.api.domain.activity.domain.ActivityGroup;
import page.clab.api.domain.activity.domain.ActivityGroupStatus;

import java.util.List;

public interface ActivityGroupRepositoryCustom {

    List<ActivityGroup> findActivityGroupsByStatus(ActivityGroupStatus status);

}
