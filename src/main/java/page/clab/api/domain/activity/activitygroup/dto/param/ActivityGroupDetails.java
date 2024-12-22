package page.clab.api.domain.activity.activitygroup.dto.param;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activity.activitygroup.domain.GroupMember;

@Getter
@Builder
public class ActivityGroupDetails {

    private ActivityGroup activityGroup;
    private List<GroupMember> groupMembers;
    private List<ActivityGroupBoard> activityGroupBoards;
}
