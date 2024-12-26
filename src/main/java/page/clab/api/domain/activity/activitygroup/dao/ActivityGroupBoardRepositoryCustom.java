package page.clab.api.domain.activity.activitygroup.dao;

import java.util.List;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoard;

public interface ActivityGroupBoardRepositoryCustom {

    List<ActivityGroupBoard> findMySubmissionsWithFeedbacks(Long parentId, String memberId);
}
