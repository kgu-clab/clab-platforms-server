package page.clab.api.domain.activity.activitygroup.dao;

import page.clab.api.domain.activity.activitygroup.dto.param.ActivityGroupDetails;

public interface ActivityGroupDetailsRepository {
    ActivityGroupDetails fetchActivityGroupDetails(Long activityGroupId);
}
