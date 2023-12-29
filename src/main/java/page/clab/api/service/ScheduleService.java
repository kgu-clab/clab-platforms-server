package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.ActivityGroupRepository;
import page.clab.api.repository.GroupMemberRepository;
import page.clab.api.repository.ScheduleRepository;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.dto.ScheduleRequestDto;
import page.clab.api.type.dto.ScheduleResponseDto;
import page.clab.api.type.entity.ActivityGroup;
import page.clab.api.type.entity.GroupMember;
import page.clab.api.type.entity.Member;
import page.clab.api.type.entity.Schedule;
import page.clab.api.type.etc.ActivityGroupRole;
import page.clab.api.type.etc.ScheduleType;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

    private final MemberService memberService;

    private final ScheduleRepository scheduleRepository;

    private final GroupMemberRepository groupMemberRepository;

    private final ActivityGroupRepository activityGroupRepository;

    public Long createSchedule(ScheduleRequestDto scheduleRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        ScheduleType scheduleType = scheduleRequestDto.getScheduleType();
        boolean isMemberAdminRole = memberService.isMemberAdminRole(member);

        if (!isMemberAdminRole && scheduleType.equals(ScheduleType.ALL)) {
            throw new PermissionDeniedException("동아리 공통 일정은 ADMIN 이상의 권한만 추가할 수 있습니다.");
        }

        if (!scheduleType.equals(ScheduleType.ALL)) {
            Long activityGroupId = Optional.ofNullable(scheduleRequestDto.getActivityGroupId())
                    .orElseThrow(() -> new NullPointerException("스터디 또는 프로젝트 일정은 그룹 id를 입력해야 합니다."));
            ActivityGroup activityGroup = activityGroupRepository.findById(activityGroupId)
                    .orElseThrow(() -> new NotFoundException("스터디 또는 프로젝트가 존재하지 않습니다."));
            GroupMember groupMember = groupMemberRepository.findByActivityGroupIdAndRole(activityGroup.getId(), ActivityGroupRole.LEADER)
                    .orElseThrow(() -> new NotFoundException("해당 스터디 또는 프로젝트에 LEADER가 존재하지 않습니다. GroupMember의 Role을 확인해주세요."));
            if (!isMemberAdminRole && !member.getId().equals(groupMember.getMember().getId())) {
                throw new PermissionDeniedException("해당 스터디 또는 프로젝트의 LEADER만 그룹 일정을 추가할 수 있습니다.");
            }
        }

        Schedule schedule = Schedule.of(scheduleRequestDto);
        schedule.setScheduleWriter(member);
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
