package page.clab.api.type.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.dto.GroupMemberResponseDto;
import page.clab.api.type.etc.ActivityGroupRole;
import page.clab.api.type.etc.GroupMemberStatus;
import page.clab.api.util.ModelMapperUtil;

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GroupMemberStatus status;

    public static GroupMember of(GroupMemberResponseDto groupMemberResponseDto) {
        return ModelMapperUtil.getModelMapper().map(groupMemberResponseDto, GroupMember.class);
    }

    public static GroupMember of(Member member, ActivityGroup activityGroup) {
        return GroupMember.builder()
                .member(member)
                .activityGroup(activityGroup)
                .build();
    }

}