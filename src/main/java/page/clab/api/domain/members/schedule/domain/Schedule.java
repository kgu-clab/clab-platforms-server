package page.clab.api.domain.members.schedule.domain;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

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
    private Boolean isDeleted;

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
            throw new BaseException(ErrorCode.INVALID_DATE_TIME_RANGE);
        }
    }

    public void validateAccessPermission(MemberDetailedInfoDto memberInfo) {
        if (!isOwner(memberInfo.getMemberId()) && !memberInfo.isAdminRole()) {
            throw new BaseException(ErrorCode.PERMISSION_DENIED, "해당 일정을 수정/삭제할 권한이 없습니다.");
        }
    }

    public void validateAccessPermissionForCreation(MemberDetailedInfoDto memberInfo) {
        if (this.getScheduleType().equals(ScheduleType.ALL) && !memberInfo.isAdminRole()) {
            throw new BaseException(ErrorCode.PERMISSION_DENIED, "동아리 공통 일정은 ADMIN 이상의 권한만 추가할 수 있습니다.");
        }
    }
}
