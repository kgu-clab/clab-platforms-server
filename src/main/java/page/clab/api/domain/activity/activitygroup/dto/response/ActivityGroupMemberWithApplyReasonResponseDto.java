package page.clab.api.domain.activity.activitygroup.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.activitygroup.domain.GroupMember;
import page.clab.api.domain.activity.activitygroup.domain.GroupMemberStatus;
import page.clab.api.domain.memberManagement.member.domain.Member;

@Getter
@Builder
public class ActivityGroupMemberWithApplyReasonResponseDto {

    private String memberId;
    private String memberName;
    private String role;
    private GroupMemberStatus status;
    private String applyReason;

    public static ActivityGroupMemberWithApplyReasonResponseDto create(Member member, GroupMember groupMember, String applyReason) {
        return ActivityGroupMemberWithApplyReasonResponseDto.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .role(groupMember.getRole().toString())
                .status(groupMember.getStatus())
                .applyReason(applyReason)
                .build();
    }
}
