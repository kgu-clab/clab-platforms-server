package page.clab.api.domain.activity.activitygroup.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.global.common.domain.BaseEntity;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@IdClass(GroupMemberId.class)
public class GroupMember extends BaseEntity {

    @Id
    private String memberId;

    @Id
    @ManyToOne
    @JoinColumn(name = "activity_group_id")
    private ActivityGroup activityGroup;

    @Enumerated(EnumType.STRING)
    private ActivityGroupRole role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GroupMemberStatus status;

    public static GroupMember create(String memberId, ActivityGroup activityGroup, ActivityGroupRole role,
        GroupMemberStatus status) {
        return GroupMember.builder()
            .memberId(memberId)
            .activityGroup(activityGroup)
            .role(role)
            .status(status)
            .build();
    }

    public boolean isLeader() {
        return role.equals(ActivityGroupRole.LEADER);
    }

    public boolean isOwner(String memberId) {
        return this.memberId.equals(memberId);
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
        return isOwner(member.getId()) && isLeader();
    }

    public boolean isAccepted() {
        return status.equals(GroupMemberStatus.ACCEPTED);
    }

    public boolean isInactive() {
        return !isAccepted() && role.isNone();
    }

    public void updateRole(ActivityGroupRole role) {
        this.role = role;
    }

    public void updateStatus(GroupMemberStatus status) {
        this.status = status;
        if (this.isAccepted()) {
            this.updateRole(ActivityGroupRole.MEMBER);
        } else {
            this.updateRole(ActivityGroupRole.NONE);
        }
    }

    public void validateAccessPermission() {
        if (this.isLeader()) {
            throw new BaseException(ErrorCode.LEADER_STATUS_CHANGE_NOT_ALLOWED);
        }
    }
}
