package page.clab.api.domain.activityGroup.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.GroupMemberStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityGroupMemberWithApplyReasonResponseDto {

    private String memberId;

    private String memberName;

    private String role;

    private GroupMemberStatus status;

    private String applyReason;

}
