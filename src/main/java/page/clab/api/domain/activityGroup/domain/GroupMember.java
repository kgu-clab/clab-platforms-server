package page.clab.api.domain.activityGroup.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.dto.response.GroupMemberResponseDto;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.util.ModelMapperUtil;

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