package page.clab.api.domain.schedule.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activityGroup.application.ActivityGroupMemberService;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.GroupMember;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.schedule.application.SchedulesWithinDateRangeRetrievalUseCase;
import page.clab.api.domain.schedule.dao.ScheduleRepository;
import page.clab.api.domain.schedule.domain.Schedule;
import page.clab.api.domain.schedule.dto.response.ScheduleResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchedulesWithinDateRangeRetrievalService implements SchedulesWithinDateRangeRetrievalUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final ActivityGroupMemberService activityGroupMemberService;
    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<ScheduleResponseDto> retrieve(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Member currentMember = memberLookupUseCase.getCurrentMember();
        List<GroupMember> groupMembers = activityGroupMemberService.getGroupMemberByMember(currentMember);
        List<ActivityGroup> myGroups = getMyActivityGroups(groupMembers);
        Page<Schedule> schedules = scheduleRepository.findByDateRangeAndMember(startDate, endDate, myGroups, pageable);
        return new PagedResponseDto<>(schedules.map(ScheduleResponseDto::toDto));
    }

    private List<ActivityGroup> getMyActivityGroups(List<GroupMember> groupMembers) {
        return groupMembers.stream()
                .map(GroupMember::getActivityGroup)
                .collect(Collectors.toList());
    }
}
