package page.clab.api.type.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.dto.GroupMemberDto;
import page.clab.api.type.etc.ActivityGroupRole;
import page.clab.api.util.ModelMapperUtil;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(GroupMemberId.class)
public class GroupMember {

    @Id
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Id
    @ManyToOne
    @JoinColumn(name = "activity_group_id")
    private ActivityGroup activityGroup;

    @Enumerated(EnumType.STRING)
    private ActivityGroupRole role;

    public static GroupMember of(GroupMemberDto groupMemberDto) {
        return ModelMapperUtil.getModelMapper().map(groupMemberDto, GroupMember.class);
    }

    public static GroupMember of(Member member, ActivityGroup activityGroup) {
        return GroupMember.builder()
                .member(member)
                .activityGroup(activityGroup)
                .role(ActivityGroupRole.LEADER)
                .build();
    }

}