package page.clab.api.domain.schedule.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.exception.InvalidDateRangeException;
import page.clab.api.global.exception.PermissionDeniedException;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
public class Schedule {

    private Long id;
    private ScheduleType scheduleType;
    private String title;
    private String detail;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private SchedulePriority priority;
    private String scheduleWriter;
    private ActivityGroup activityGroup;
    private boolean isDeleted = false;


    public void delete() {
        this.isDeleted = true;
    }

    public boolean isOwner(String memberId) {
        return this.scheduleWriter.equals(memberId);
    }

    public boolean isAllSchedule() {
        return this.scheduleType.equals(ScheduleType.ALL);
    }

    public void validateBusinessRules() {
        if (startDateTime.isAfter(endDateTime)) {
            throw new InvalidDateRangeException("시작일시가 종료일시보다 늦을 수 없습니다.");
        }
    }

    public void validateAccessPermission(MemberDetailedInfoDto memberInfo) throws PermissionDeniedException {
        if (!isOwner(memberInfo.getMemberId()) && !memberInfo.isAdminRole()) {
            throw new PermissionDeniedException("해당 일정을 수정/삭제할 권한이 없습니다.");
        }
    }

    public void validateAccessPermissionForCreation(MemberDetailedInfoDto memberInfo) throws PermissionDeniedException {
        if (this.getScheduleType().equals(ScheduleType.ALL) && !memberInfo.isAdminRole()) {
            throw new PermissionDeniedException("동아리 공통 일정은 ADMIN 이상의 권한만 추가할 수 있습니다.");
        }
    }
}
