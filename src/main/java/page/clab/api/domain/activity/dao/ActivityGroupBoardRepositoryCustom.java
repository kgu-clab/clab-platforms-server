package page.clab.api.domain.activity.dao;

import page.clab.api.domain.activity.domain.ActivityGroupBoard;

import java.util.List;

public interface ActivityGroupBoardRepositoryCustom {

    List<ActivityGroupBoard> findMySubmissionsWithFeedbacks(Long parentId, String memberId);

}
