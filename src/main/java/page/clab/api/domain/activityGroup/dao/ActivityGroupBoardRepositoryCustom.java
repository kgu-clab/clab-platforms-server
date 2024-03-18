package page.clab.api.domain.activityGroup.dao;

import page.clab.api.domain.activityGroup.domain.ActivityGroupBoard;

import java.util.List;

public interface ActivityGroupBoardRepositoryCustom {

    List<ActivityGroupBoard> findMySubmissionsWithFeedbacks(Long parentId, String memberId);

}
