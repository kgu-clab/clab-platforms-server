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
import page.clab.api.domain.activityGroup.exception.LeaderStatusChangeNotAllowedException;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.domain.BaseEntity;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(GroupMemberId.class)
public class GroupMember extends BaseEntity {

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

    public static GroupMember create(Member member, ActivityGroup activityGroup, ActivityGroupRole role, GroupMemberStatus status) {
        return GroupMember.builder()
                .member(member)
                .activityGroup(activityGroup)
                .role(role)
                .status(status)
                .build();
    }

    public boolean isLeader() {
        return role.equals(ActivityGroupRole.LEADER);
    }

    public boolean isOwner(Member member) {
        return this.member.isSameMember(member);
    }

    public boolean isSameRole(ActivityGroupRole role) {
        return this.role == role;
    }

    public boolean isSameActivityGroup(ActivityGroup activityGroup) {
        return this.activityGroup.equals(activityGroup);
    }

    public boolean isSameRoleAndActivityGroup(ActivityGroupRole role, ActivityGroup activityGroup) {
        return isSameRole(role) && isSameActivityGroup(activityGroup);
    }

    public boolean isOwnerAndLeader(Member member) {
        return isOwner(member) && isLeader();
    }

    public boolean isAccepted() {
        return status.equals(GroupMemberStatus.ACCEPTED);
    }

    public void updateRole(ActivityGroupRole role) {
        this.role = role;
    }

    public void updateStatus(GroupMemberStatus status) {
        this.status = status;
        if (this.isAccepted()) {
            this.updateRole(ActivityGroupRole.MEMBER);
        } else {
            this.updateRole(null);
        }
    }

    public void validateAccessPermission() {
        if (this.isLeader()) {
            throw new LeaderStatusChangeNotAllowedException("리더의 상태는 변경할 수 없습니다.");
        }
    }

}