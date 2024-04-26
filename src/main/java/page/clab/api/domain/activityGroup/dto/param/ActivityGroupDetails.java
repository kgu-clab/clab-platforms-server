package page.clab.api.domain.activityGroup.dto.param;

import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activityGroup.domain.GroupMember;

import java.util.List;

@Getter
@Setter
public class ActivityGroupDetails {

    private ActivityGroup activityGroup;

    private List<GroupMember> groupMembers;

    private List<ActivityGroupBoard> activityGroupBoards;

    private ActivityGroupDetails(ActivityGroup activityGroup, List<GroupMember> groupMembers, List<ActivityGroupBoard> activityGroupBoards) {
        this.activityGroup = activityGroup;
        this.groupMembers = groupMembers;
        this.activityGroupBoards = activityGroupBoards;
    }

    public static ActivityGroupDetails create(ActivityGroup activityGroup, List<GroupMember> groupMembers, List<ActivityGroupBoard> activityGroupBoards) {
        return new ActivityGroupDetails(activityGroup, groupMembers, activityGroupBoards);
    }

}
