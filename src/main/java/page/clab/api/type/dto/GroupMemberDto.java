package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.ActivityGroup;
import page.clab.api.type.entity.GroupMember;
import page.clab.api.type.entity.Member;
import page.clab.api.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupMemberDto {

    Member member;

    ActivityGroup activityGroup;

    String role;

    public static GroupMemberDto of(GroupMember groupMember) {
        return ModelMapperUtil.getModelMapper().map(groupMember, GroupMemberDto.class);
    }
}
