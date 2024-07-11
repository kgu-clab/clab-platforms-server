package page.clab.api.domain.activity.activitygroup.dao;

import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoard;

import java.util.List;

public interface ActivityGroupBoardRepositoryCustom {

    List<ActivityGroupBoard> findMySubmissionsWithFeedbacks(Long parentId, String memberId);

}
