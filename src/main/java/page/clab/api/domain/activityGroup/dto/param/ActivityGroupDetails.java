package page.clab.api.domain.activityGroup.dto.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activityGroup.domain.GroupMember;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityGroupDetails {

    private ActivityGroup activityGroup;

    private List<GroupMember> groupMembers;

    private List<ActivityGroupBoard> activityGroupBoards;

}
