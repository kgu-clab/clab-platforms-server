package page.clab.api.domain.activity.dao;

import page.clab.api.domain.activity.dto.param.ActivityGroupDetails;

public interface ActivityGroupDetailsRepository {

    ActivityGroupDetails fetchActivityGroupDetails(Long activityGroupId);

}
