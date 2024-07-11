package page.clab.api.domain.schedule.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activity.activitygroup.application.ActivityGroupAdminService;
import page.clab.api.domain.activity.activitygroup.application.ActivityGroupMemberService;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupRole;
import page.clab.api.domain.activity.activitygroup.domain.GroupMember;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.schedule.application.dto.request.ScheduleRequestDto;
import page.clab.api.domain.schedule.application.port.in.RegisterScheduleUseCase;
import page.clab.api.domain.schedule.application.port.out.RegisterSchedulePort;
import page.clab.api.domain.schedule.domain.Schedule;
import page.clab.api.domain.schedule.domain.ScheduleType;
import page.clab.api.global.exception.PermissionDeniedException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleRegisterService implements RegisterScheduleUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final ActivityGroupMemberService activityGroupMemberService;
    private final ActivityGroupAdminService activityGroupAdminService;
    private final RegisterSchedulePort registerSchedulePort;

    @Override
    @Transactional
    public Long registerSchedule(ScheduleRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberDetailedInfo();
        ActivityGroup activityGroup = resolveActivityGroupForSchedule(requestDto, currentMemberInfo);
        Schedule schedule = ScheduleRequestDto.toEntity(requestDto, currentMemberInfo.getMemberId(), activityGroup);
        schedule.validateAccessPermissionForCreation(currentMemberInfo);
        schedule.validateBusinessRules();
        return registerSchedulePort.save(schedule).getId();
    }

    private ActivityGroup resolveActivityGroupForSchedule(ScheduleRequestDto requestDto, MemberDetailedInfoDto memberInfo) throws PermissionDeniedException {
        ScheduleType scheduleType = requestDto.getScheduleType();
        ActivityGroup activityGroup = null;
        if (!scheduleType.equals(ScheduleType.ALL)) {
            Long activityGroupId = Optional.ofNullable(requestDto.getActivityGroupId())
                    .orElseThrow(() -> new NullPointerException("스터디 또는 프로젝트 일정은 그룹 id를 입력해야 합니다."));
            activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);
            validateMemberIsGroupLeaderOrAdmin(memberInfo, activityGroup);
        }
        return activityGroup;
    }

    private void validateMemberIsGroupLeaderOrAdmin(MemberDetailedInfoDto memberInfo, ActivityGroup activityGroup) throws PermissionDeniedException {
        GroupMember groupMember = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(activityGroup.getId(), ActivityGroupRole.LEADER);
        if (groupMember != null && !memberInfo.isAdminRole() && !groupMember.isOwner(memberInfo.getMemberId())) {
            throw new PermissionDeniedException("해당 스터디 또는 프로젝트의 LEADER, 관리자만 그룹 일정을 추가할 수 있습니다.");
        }
    }
}
