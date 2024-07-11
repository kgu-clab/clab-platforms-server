package page.clab.api.domain.schedule.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activity.application.ActivityGroupMemberService;
import page.clab.api.domain.activity.domain.ActivityGroup;
import page.clab.api.domain.activity.domain.GroupMember;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.schedule.application.dto.response.ScheduleResponseDto;
import page.clab.api.domain.schedule.application.port.in.RetrieveSchedulesWithinDateRangeUseCase;
import page.clab.api.domain.schedule.application.port.out.RetrieveSchedulePort;
import page.clab.api.domain.schedule.domain.Schedule;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchedulesWithinDateRangeRetrievalService implements RetrieveSchedulesWithinDateRangeUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final ActivityGroupMemberService activityGroupMemberService;
    private final RetrieveSchedulePort retrieveSchedulePort;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<ScheduleResponseDto> retrieveSchedulesWithinDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        String currentMemberId = retrieveMemberUseCase.getCurrentMemberId();
        List<GroupMember> groupMembers = activityGroupMemberService.getGroupMemberByMemberId(currentMemberId);
        List<ActivityGroup> myGroups = getMyActivityGroups(groupMembers);
        Page<Schedule> schedules = retrieveSchedulePort.findByDateRangeAndMember(startDate, endDate, myGroups, pageable);
        return new PagedResponseDto<>(schedules.map(ScheduleResponseDto::toDto));
    }

    private List<ActivityGroup> getMyActivityGroups(List<GroupMember> groupMembers) {
        return groupMembers.stream()
                .map(GroupMember::getActivityGroup)
                .collect(Collectors.toList());
    }
}
