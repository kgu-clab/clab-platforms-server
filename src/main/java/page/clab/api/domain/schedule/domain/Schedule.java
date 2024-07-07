package page.clab.api.domain.schedule.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.domain.BaseEntity;
import page.clab.api.global.exception.PermissionDeniedException;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@SQLDelete(sql = "UPDATE schedule SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String detail;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SchedulePriority priority;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member scheduleWriter;

    @ManyToOne
    @JoinColumn(name = "activityGroup")
    private ActivityGroup activityGroup;

    public void delete() {
        this.isDeleted = true;
    }

    public boolean isOwner(Member member) {
        return this.scheduleWriter.isSameMember(member);
    }

    public boolean isAllSchedule() {
        return this.scheduleType.equals(ScheduleType.ALL);
    }

    public void validateAccessPermission(Member member) throws PermissionDeniedException {
        if (!isOwner(member) && !member.isAdminRole()) {
            throw new PermissionDeniedException("해당 일정을 수정/삭제할 권한이 없습니다.");
        }
    }

    public void validateAccessPermissionForCreation(Member member) throws PermissionDeniedException {
        if (this.getScheduleType().equals(ScheduleType.ALL) && !member.isAdminRole()) {
            throw new PermissionDeniedException("동아리 공통 일정은 ADMIN 이상의 권한만 추가할 수 있습니다.");
        }
    }
}
