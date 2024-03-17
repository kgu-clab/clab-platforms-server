package page.clab.api.domain.schedule.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        validateScheduleCreationPermission(scheduleRequestDto, member);
        ActivityGroup activityGroup = resolveActivityGroupForSchedule(scheduleRequestDto, member);
        Schedule schedule = Schedule.create(scheduleRequestDto, member, activityGroup);
        return scheduleRepository.save(schedule).getId();
    }

    public PagedResponseDto<ScheduleResponseDto> getSchedulesWithinDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable) {
        Member member = memberService.getCurrentMember();
        List<Schedule> mySchedules = getSchedulesWithinDateRange(startDateTime, endDateTime, member);
        Page<Schedule> myPagedSchedules = new PageImpl<>(mySchedules, pageable, mySchedules.size());
        return new PagedResponseDto<>(myPagedSchedules.map(ScheduleResponseDto::of));
    }

    public PagedResponseDto<ScheduleResponseDto> getActivitySchedules(LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable) {
        Member member = memberService.getCurrentMember();
        List<Schedule> schedules = getSchedulesWithinDateRange(startDateTime, endDateTime, member).stream()
                .filter(this::isActivitySchedule)
                .toList();
        Page<Schedule> myPagedActivitySchedules = new PageImpl<>(schedules, pageable, schedules.size());
        return new PagedResponseDto<>(myPagedActivitySchedules.map(ScheduleResponseDto::of));
    }

    public Long deleteSchedule(Long scheduleId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Schedule schedule = getScheduleById(scheduleId);
        validateScheduleDeletionPermission(member, schedule);
        scheduleRepository.delete(schedule);
        return schedule.getId();
    }

    private void validateScheduleCreationPermission(ScheduleRequestDto scheduleRequestDto, Member member) throws PermissionDeniedException {
        if (scheduleRequestDto.getScheduleType().equals(ScheduleType.ALL) && !memberService.isMemberAdminRole(member)) {
            throw new PermissionDeniedException("동아리 공통 일정은 ADMIN 이상의 권한만 추가할 수 있습니다.");
        }
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
        if (groupMember != null &&
                !memberService.isMemberAdminRole(member) &&
                !member.getId().equals(groupMember.getMember().getId())
        ) {
            throw new PermissionDeniedException("해당 스터디 또는 프로젝트의 LEADER, 관리자만 그룹 일정을 추가할 수 있습니다.");
        }
    }

    public List<Schedule> getSchedulesWithinDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime, Member member) {
        List<GroupMember> groupMemberList = activityGroupMemberService.getGroupMemberByMember(member)
                .stream()
                .distinct()
                .toList();

        List<Long> activityGroupIdList = groupMemberList.stream()
                .map(GroupMember::getActivityGroup)
                .map(ActivityGroup::getId)
                .collect(Collectors.toList());

        List<Schedule> validDateSchedules = getScheduleByDateBetween(startDateTime, endDateTime)
                .stream()
                .sorted(Comparator.comparing(Schedule::getStartDateTime))
                .toList();

        return validDateSchedules.stream()
                .filter(schedule -> isValid(schedule, activityGroupIdList))
                .collect(Collectors.toList());
    }

    public List<Schedule> getScheduleByDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime){
        return scheduleRepository.findAllByStartDateTimeBetween(startDateTime, endDateTime);
    }

    public Schedule getScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundException("일정이 존재하지 않습니다."));
    }

    private void validateScheduleDeletionPermission(Member member, Schedule schedule) throws PermissionDeniedException {
        if (!(member.getId().equals(schedule.getScheduleWriter().getId()) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException("해당 일정을 삭제할 권한이 없습니다.");
        }
    }

    private boolean isActivitySchedule(Schedule schedule) {
        return schedule.getScheduleType() != ScheduleType.ALL;
    }

    private boolean isValid(Schedule schedule, List<Long> activityGroupIdList) {
        if (schedule.getScheduleType() == ScheduleType.ALL) {
            return true;
        }
        Long activityGroupId = schedule.getActivityGroup().getId();
        return activityGroupIdList.contains(activityGroupId);
    }

}
