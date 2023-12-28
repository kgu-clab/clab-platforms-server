package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.ActivityGroupRepository;
import page.clab.api.repository.ScheduleRepository;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.dto.ScheduleRequestDto;
import page.clab.api.type.dto.ScheduleResponseDto;
import page.clab.api.type.entity.ActivityGroup;
import page.clab.api.type.entity.Member;
import page.clab.api.type.entity.Schedule;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

    private final MemberService memberService;

    private final ScheduleRepository scheduleRepository;

    private final ActivityGroupRepository activityGroupRepository;

    public Long createSchedule(ScheduleRequestDto scheduleRequestDto){
        Member member = memberService.getCurrentMember();
        ActivityGroup activityGroup = null;
        Schedule schedule = Schedule.of(scheduleRequestDto);
        schedule.setIsPublic(true);
        schedule.setScheduleWriter(member);

        if(scheduleRequestDto.getActivityGroupId() != null){
            activityGroup = activityGroupRepository.findById(scheduleRequestDto.getActivityGroupId()).get();
            schedule.setActivityGroup(activityGroup);
            schedule.setIsPublic(false);
        }

        Long id = scheduleRepository.save(schedule).getId();
        return id;
    }

    public PagedResponseDto<ScheduleResponseDto> getSchedules(String startDate, String endDate, Pageable pageable){
        return null;
    }

    public Long deleteSchedule(Long scheduleId) throws PermissionDeniedException{
        Member member = memberService.getCurrentMember();
        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        if (!(member.getId().equals(schedule.getScheduleWriter().getId()) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException("해당 리뷰를 삭제할 권한이 없습니다.");
        }

        scheduleRepository.delete(schedule);
        return schedule.getId();
    }
}
