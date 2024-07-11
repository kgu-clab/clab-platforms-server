package page.clab.api.domain.activity.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.domain.GroupMember;
import page.clab.api.domain.activity.domain.GroupMemberStatus;
import page.clab.api.domain.member.domain.Member;

@Getter
@Builder
public class GroupMemberResponseDto {

    private String memberId;

    private String memberName;

    private String role;

    private GroupMemberStatus status;

    public static GroupMemberResponseDto toDto(Member member, GroupMember groupMember) {
        return GroupMemberResponseDto.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .role(groupMember.getRole().getKey())
                .status(groupMember.getStatus())
                .build();
    }

}
