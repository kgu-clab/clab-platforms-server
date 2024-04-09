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
import page.clab.api.domain.schedule.domain.SchedulePriority;
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

    public Long createSchedule(ScheduleRequestDto requestDto) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        ActivityGroup activityGroup = resolveActivityGroupForSchedule(requestDto, currentMember);
        Schedule schedule = ScheduleRequestDto.toEntity(requestDto, currentMember, activityGroup);
        schedule.validateAccessPermissionForCreation(currentMember);
        return scheduleRepository.save(schedule).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ScheduleResponseDto> getSchedulesWithinDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Member currentMember = memberService.getCurrentMember();
        Page<Schedule> schedules = scheduleRepository.findByDateRangeAndMember(startDate, endDate, currentMember, pageable);
        return new PagedResponseDto<>(schedules.map(ScheduleResponseDto::toDto));
    }

    public PagedResponseDto<ScheduleResponseDto> getSchedulesByConditions(Integer year, Integer month, SchedulePriority priority, Pageable pageable) {
        Page<Schedule> schedules = scheduleRepository.findByConditions(year, month, priority, pageable);
        return new PagedResponseDto<>(schedules.map(ScheduleResponseDto::toDto));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ScheduleResponseDto> getActivitySchedules(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Member currentMember = memberService.getCurrentMember();
        Page<Schedule> schedules = scheduleRepository.findActivitySchedulesByDateRangeAndMember(startDate, endDate, currentMember, pageable);
        return new PagedResponseDto<>(schedules.map(ScheduleResponseDto::toDto));
    }

    public Long deleteSchedule(Long scheduleId) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        Schedule schedule = getScheduleById(scheduleId);
        schedule.validateAccessPermission(currentMember);
        scheduleRepository.delete(schedule);
        return schedule.getId();
    }

    private ActivityGroup resolveActivityGroupForSchedule(ScheduleRequestDto requestDto, Member member) throws PermissionDeniedException {
        ScheduleType scheduleType = requestDto.getScheduleType();
        ActivityGroup activityGroup = null;
        if (!scheduleType.equals(ScheduleType.ALL)) {
            Long activityGroupId = Optional.ofNullable(requestDto.getActivityGroupId())
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
