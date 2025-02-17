package page.clab.api.domain.members.schedule.application.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import page.clab.api.domain.activity.activitygroup.application.ActivityGroupAdminService;
import page.clab.api.domain.activity.activitygroup.application.ActivityGroupMemberService;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupRole;
import page.clab.api.domain.activity.activitygroup.domain.GroupMember;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.members.schedule.application.dto.mapper.ScheduleDtoMapper;
import page.clab.api.domain.members.schedule.application.dto.request.ScheduleRequestDto;
import page.clab.api.domain.members.schedule.application.port.in.RegisterScheduleUseCase;
import page.clab.api.domain.members.schedule.application.port.out.RegisterSchedulePort;
import page.clab.api.domain.members.schedule.domain.Schedule;
import page.clab.api.domain.members.schedule.domain.ScheduleType;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class ScheduleRegisterService implements RegisterScheduleUseCase {

    private final RegisterSchedulePort registerSchedulePort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ActivityGroupMemberService activityGroupMemberService;
    private final ActivityGroupAdminService activityGroupAdminService;
    private final ScheduleDtoMapper mapper;

    @Override
    @Transactional
    public Long registerSchedule(ScheduleRequestDto requestDto) {
        MemberDetailedInfoDto currentMemberInfo = externalRetrieveMemberUseCase.getCurrentMemberDetailedInfo();
        ActivityGroup activityGroup = resolveActivityGroupForSchedule(requestDto, currentMemberInfo);
        Schedule schedule = mapper.fromDto(requestDto, currentMemberInfo.getMemberId(), activityGroup);
        schedule.validateAccessPermissionForCreation(currentMemberInfo);
        schedule.validateBusinessRules();
        return registerSchedulePort.save(schedule).getId();
    }

    private ActivityGroup resolveActivityGroupForSchedule(
        ScheduleRequestDto requestDto,
        MemberDetailedInfoDto memberInfo
    ) {
        ScheduleType scheduleType = requestDto.getScheduleType();
        ActivityGroup activityGroup = null;
        if (!scheduleType.equals(ScheduleType.ALL)) {
            Long activityGroupId = Optional.ofNullable(requestDto.getActivityGroupId())
                .orElseThrow(() -> new NullPointerException("스터디 또는 프로젝트 일정은 그룹 id를 입력해야 합니다."));
            activityGroup = activityGroupAdminService.getActivityGroupById(activityGroupId);
            validateMemberIsGroupLeaderOrAdmin(memberInfo, activityGroup);
        }
        return activityGroup;
    }

    private void validateMemberIsGroupLeaderOrAdmin(MemberDetailedInfoDto memberInfo, ActivityGroup activityGroup) {
        List<GroupMember> groupLeaders = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(
            activityGroup.getId(), ActivityGroupRole.LEADER);
        if (!CollectionUtils.isEmpty(groupLeaders) && !memberInfo.isAdminRole() && groupLeaders.stream()
            .noneMatch(leader -> leader.isOwner(memberInfo.getMemberId()))) {
            throw new BaseException(ErrorCode.PERMISSION_DENIED, "해당 스터디 또는 프로젝트의 LEADER, 관리자만 그룹 일정을 추가할 수 있습니다.");
        }
    }
}
