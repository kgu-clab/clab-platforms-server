package page.clab.api.domain.activityGroup.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.GroupMember;
import page.clab.api.domain.activityGroup.domain.GroupMemberStatus;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupMemberResponseDto {

    private String memberId;

    private String memberName;

    private String role;

    private GroupMemberStatus status;

    public static GroupMemberResponseDto toDto(GroupMember groupMember) {
        return GroupMemberResponseDto.builder()
                .memberId(groupMember.getMember().getId())
                .memberName(groupMember.getMember().getName())
                .role(groupMember.getRole().getKey())
                .status(groupMember.getStatus())
                .build();
    }

    public static List<GroupMemberResponseDto> toDto(List<GroupMember> groupMembers) {
        return groupMembers.stream()
                .map(GroupMemberResponseDto::toDto)
                .toList();
    }

}
