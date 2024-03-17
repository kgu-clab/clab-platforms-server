package page.clab.api.domain.activityGroup.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.GroupMember;
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

    public static ActivityGroupMemberWithApplyReasonResponseDto create(GroupMember groupMember, String applyReason) {
        return ActivityGroupMemberWithApplyReasonResponseDto.builder()
                .memberId(groupMember.getMember().getId())
                .memberName(groupMember.getMember().getName())
                .role(groupMember.getRole().toString())
                .status(groupMember.getStatus())
                .applyReason(applyReason)
                .build();
    }

}
