package page.clab.api.domain.activityGroup.dao;

import page.clab.api.domain.activityGroup.dto.param.ActivityGroupDetails;

public interface ActivityGroupDetailsRepository {

    ActivityGroupDetails fetchActivityGroupDetails(Long activityGroupId);

}
