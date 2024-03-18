package page.clab.api.domain.schedule.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activityGroup.application.ActivityGroupAdminService;
import page.clab.api.domain.activityGroup.application.ActivityGroupMemberService;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupRole;
import page.clab.api.domain.activityGroup.domain.GroupMember;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.schedule.dao.ScheduleRepository;
import page.clab.api.domain.schedule.domain.Schedule;
import page.clab.api.domain.schedule.domain.ScheduleType;
import page.clab.api.domain.schedule.dto.request.ScheduleRequestDto;
import page.clab.api.domain.schedule.dto.response.ScheduleResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

    private final MemberService memberService;

    private final ActivityGroupMemberService activityGroupMemberService;

    private final ActivityGroupAdminService activityGroupAdminService;

    private final ScheduleRepository scheduleRepository;

    public Long createSchedule(ScheduleRequestDto scheduleRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        ActivityGroup activityGroup = resolveActivityGroupForSchedule(scheduleRequestDto, member);
        Schedule schedule = Schedule.create(scheduleRequestDto, member, activityGroup);
        return scheduleRepository.save(schedule).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ScheduleResponseDto> getSchedulesWithinDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Member member = memberService.getCurrentMember();
        Page<Schedule> schedules = scheduleRepository.findByDateRangeAndMember(startDate, endDate, member, pageable);
        return new PagedResponseDto<>(schedules.map(ScheduleResponseDto::of));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ScheduleResponseDto> getActivitySchedules(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Member member = memberService.getCurrentMember();
        Page<Schedule> schedules = scheduleRepository.findActivitySchedulesByDateRangeAndMember(startDate, endDate, member, pageable);
        return new PagedResponseDto<>(schedules.map(ScheduleResponseDto::of));
    }

    public Long deleteSchedule(Long scheduleId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Schedule schedule = getScheduleById(scheduleId);
        schedule.validateAccessPermission(member);
        scheduleRepository.delete(schedule);
        return schedule.getId();
    }

    private ActivityGroup resolveActivityGroupForSchedule(ScheduleRequestDto scheduleRequestDto, Member member) throws PermissionDeniedException {
        ScheduleType scheduleType = scheduleRequestDto.getScheduleType();
        ActivityGroup activityGroup = null;
        if (!scheduleType.equals(ScheduleType.ALL)) {
            Long activityGroupId = Optional.ofNullable(scheduleRequestDto.getActivityGroupId())
                    .orElseThrow(() -> new NullPointerException("스터디 또는 프로젝트 일정은 그룹 id를 입력해야 합니다."));
            activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);
            validateMemberIsGroupLeaderOrAdmin(member, activityGroup);
        }
        return activityGroup;
    }

    private void validateMemberIsGroupLeaderOrAdmin(Member member, ActivityGroup activityGroup) throws PermissionDeniedException {
        GroupMember groupMember = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(activityGroup.getId(), ActivityGroupRole.LEADER);
        if (groupMember != null && !member.isAdminRole() && !member.isSameMember(groupMember.getMember())) {
            throw new PermissionDeniedException("해당 스터디 또는 프로젝트의 LEADER, 관리자만 그룹 일정을 추가할 수 있습니다.");
        }
    }

    public Schedule getScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundException("일정이 존재하지 않습니다."));
    }

}
