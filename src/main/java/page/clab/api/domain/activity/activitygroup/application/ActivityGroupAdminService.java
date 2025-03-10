package page.clab.api.domain.activity.activitygroup.application;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import page.clab.api.domain.activity.activitygroup.dao.ActivityGroupRepository;
import page.clab.api.domain.activity.activitygroup.dao.ApplyFormRepository;
import page.clab.api.domain.activity.activitygroup.dao.GroupScheduleRepository;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupRole;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupStatus;
import page.clab.api.domain.activity.activitygroup.domain.ApplyForm;
import page.clab.api.domain.activity.activitygroup.domain.GroupMember;
import page.clab.api.domain.activity.activitygroup.domain.GroupMemberStatus;
import page.clab.api.domain.activity.activitygroup.domain.GroupSchedule;
import page.clab.api.domain.activity.activitygroup.dto.mapper.ActivityGroupDtoMapper;
import page.clab.api.domain.activity.activitygroup.dto.param.GroupScheduleDto;
import page.clab.api.domain.activity.activitygroup.dto.request.ActivityGroupRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.request.ActivityGroupUpdateRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupBoardStatusUpdatedResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupMemberWithApplyReasonResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupResponseDto;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.external.memberManagement.notification.application.port.ExternalSendNotificationUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class ActivityGroupAdminService {

    private final ActivityGroupMemberService activityGroupMemberService;
    private final ActivityGroupRepository activityGroupRepository;
    private final GroupScheduleRepository groupScheduleRepository;
    private final ApplyFormRepository applyFormRepository;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalSendNotificationUseCase externalSendNotificationUseCase;
    private final ActivityGroupDtoMapper mapper;

    @Transactional
    public Long createActivityGroup(ActivityGroupRequestDto requestDto) {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = mapper.fromDto(requestDto);
        activityGroup.validateAndSetGithubUrl(activityGroup.getGithubUrl());
        activityGroupRepository.save(activityGroup);

        GroupMember groupLeader = GroupMember.create(currentMember.getId(), activityGroup, ActivityGroupRole.LEADER,
            GroupMemberStatus.ACCEPTED);
        activityGroupMemberService.save(groupLeader);

        externalSendNotificationUseCase.sendNotificationToMember(currentMember.getId(),
            "활동 그룹 생성이 완료되었습니다. 활동 승인이 완료되면 활동 그룹을 이용할 수 있습니다.");
        return activityGroup.getId();
    }

    @Transactional
    public Long updateActivityGroup(Long activityGroupId, ActivityGroupUpdateRequestDto requestDto) {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupById(activityGroupId);
        validateLeaderOrAdminPermission(activityGroup, currentMember, "해당 활동을 수정할 권한이 없습니다.");
        activityGroup.update(requestDto);
        return activityGroupRepository.save(activityGroup).getId();
    }

    // 활동 그룹의 status를 수정합니다.
    @Transactional
    public ActivityGroupBoardStatusUpdatedResponseDto manageActivityGroup(Long activityGroupId,
        ActivityGroupStatus status) {
        ActivityGroup activityGroup = getActivityGroupById(activityGroupId);
        activityGroup.updateStatus(status);
        activityGroupRepository.save(activityGroup);

        List<GroupMember> groupLeaders = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(
            activityGroupId, ActivityGroupRole.LEADER);
        if (!CollectionUtils.isEmpty(groupLeaders)) {
            groupLeaders.forEach(
                leader -> externalSendNotificationUseCase.sendNotificationToMember(leader.getMemberId(),
                    "활동 그룹이 [" + status.getDescription() + "] 상태로 변경되었습니다."));
        }
        return mapper.of(activityGroupId, status);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupResponseDto> getDeletedActivityGroups(Pageable pageable) {
        Page<ActivityGroup> activityGroups = activityGroupRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(activityGroups.map(mapper::toDto));
    }

    @Transactional
    public Long deleteActivityGroup(Long activityGroupId) {
        ActivityGroup activityGroup = getActivityGroupById(activityGroupId);
        List<GroupMember> groupMembers = activityGroupMemberService.getGroupMemberByActivityGroupId(activityGroupId);
        List<GroupSchedule> groupSchedules = groupScheduleRepository.findAllByActivityGroupIdOrderByIdDesc(
            activityGroupId);
        List<GroupMember> groupLeaders = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(
            activityGroupId, ActivityGroupRole.LEADER);

        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        validateLeaderOrAdminPermission(activityGroup, currentMember, "해당 활동을 삭제할 권한이 없습니다.");

        activityGroupMemberService.deleteAll(groupMembers);
        groupScheduleRepository.deleteAll(groupSchedules);
        activityGroupRepository.delete(activityGroup);

        if (!CollectionUtils.isEmpty(groupLeaders)) {
            groupLeaders.forEach(
                leader -> externalSendNotificationUseCase.sendNotificationToMember(leader.getMemberId(),
                    "활동 그룹 [" + activityGroup.getName() + "]이 삭제되었습니다."));
        }
        return activityGroup.getId();
    }

    @Transactional
    public Long updateProjectProgress(Long activityGroupId, Long progress) {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupById(activityGroupId);
        validateLeaderOrAdminPermission(activityGroup, currentMember, "해당 활동을 수정할 권한이 업습니다.");
        activityGroup.updateProgress(progress);
        return activityGroupRepository.save(activityGroup).getId();
    }

    @Transactional
    public Long addSchedule(Long activityGroupId, List<GroupScheduleDto> scheduleDtos) {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupById(activityGroupId);
        validateLeaderOrAdminPermission(activityGroup, currentMember, "해당 일정을 등록할 권한이 없습니다.");
        List<GroupSchedule> groupSchedules = scheduleDtos.stream()
            .map(scheduleDto -> mapper.fromDto(scheduleDto, activityGroup))
            .toList();
        groupScheduleRepository.saveAll(groupSchedules);
        return activityGroup.getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupMemberWithApplyReasonResponseDto> getGroupMembersWithApplyReason(
        Long activityGroupId, Pageable pageable) {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupById(activityGroupId);
        validateLeaderOrAdminPermission(activityGroup, currentMember, "해당 활동의 멤버를 조회할 권한이 없습니다.");

        List<ApplyForm> applyForms = applyFormRepository.findAllByActivityGroup(activityGroup);
        Map<String, String> memberIdToApplyReasonMap = applyForms.stream()
            .collect(Collectors.toMap(
                ApplyForm::getMemberId,
                ApplyForm::getApplyReason
            ));

        Page<GroupMember> groupMembers = activityGroupMemberService.getGroupMemberByActivityGroupId(activityGroupId,
            pageable);
        List<ActivityGroupMemberWithApplyReasonResponseDto> groupMembersWithApplyReason = groupMembers.getContent()
            .stream()
            .map(groupMember -> {
                String applyReason = memberIdToApplyReasonMap.getOrDefault(groupMember.getMemberId(), "");
                Member member = externalRetrieveMemberUseCase.getById(groupMember.getMemberId());
                return mapper.toDto(member, groupMember, applyReason);
            })
            .toList();

        Page<ActivityGroupMemberWithApplyReasonResponseDto> paginatedGroupMembersWithApplyReason = new PageImpl<>(
            groupMembersWithApplyReason, pageable, groupMembers.getTotalElements());
        return new PagedResponseDto<>(paginatedGroupMembersWithApplyReason, groupMembers.getTotalElements(),
            groupMembersWithApplyReason.size());
    }

    // 활동 멤버들의 status를 수정합니다.
    @Transactional
    public Long manageGroupMemberStatus(Long activityGroupId, List<String> memberIds, GroupMemberStatus status) {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupById(activityGroupId);
        validateLeaderOrAdminPermission(activityGroup, currentMember, "해당 활동의 신청 멤버를 조회할 권한이 없습니다.");
        memberIds.forEach(memberId -> updateGroupMemberStatus(memberId, status, activityGroup));
        return activityGroup.getId();
    }

    @Transactional
    public Long changeGroupMemberPosition(Long activityGroupId, String memberId, ActivityGroupRole position) {
        ActivityGroup activityGroup = getActivityGroupById(activityGroupId);
        GroupMember groupMember = activityGroupMemberService.getGroupMemberByActivityGroupAndMember(activityGroup,
            memberId);
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();

        validateLeaderOrAdminPermission(activityGroup, currentMember, "해당 활동의 멤버 직책을 변경할 권한이 없습니다.");
        validateLeaderRoleChange(activityGroup, groupMember);
        validateMemberIsActive(groupMember);
        validateNewPosition(groupMember, position);

        groupMember.updateRole(position);
        activityGroupMemberService.save(groupMember);

        return activityGroup.getId();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateActivityGroupStatusEnd() {
        List<ActivityGroup> activityGroups = activityGroupRepository.findByEndDateBeforeAndStatusNot(LocalDate.now(),
            ActivityGroupStatus.END);
        if (!activityGroups.isEmpty()) {
            activityGroups.forEach(this::updateActivityGroupStatusEnd);
        }
    }

    private void updateActivityGroupStatusEnd(ActivityGroup activityGroup) {
        activityGroup.updateStatus(ActivityGroupStatus.END);
        activityGroupRepository.save(activityGroup);
    }

    private void updateGroupMemberStatus(String memberId, GroupMemberStatus status, ActivityGroup activityGroup) {
        Member member = externalRetrieveMemberUseCase.getById(memberId);
        GroupMember groupMember = activityGroupMemberService.getGroupMemberByActivityGroupAndMember(activityGroup,
            member.getId());
        groupMember.validateAccessPermission();
        groupMember.updateStatus(status);
        activityGroupMemberService.save(groupMember);
        externalSendNotificationUseCase.sendNotificationToMember(member.getId(),
            "[" + activityGroup.getName() + "]" + " 신청이 [" + status.getDescription() + "] 상태로 변경되었습니다.");
    }

    public ActivityGroup getActivityGroupById(Long activityGroupId) {
        return activityGroupRepository.findById(activityGroupId)
            .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND, "존재하지 않는 활동입니다."));
    }

    // 해당 멤버가 특정 활동 그룹의 리더 또는 관리자인지 검증합니다.
    // 예외가 발생하지 않고 안전하게 처리됩니다.
    public boolean hasLeaderOrAdminRole(ActivityGroup activityGroup, Member member) {
        return activityGroupMemberService.findGroupMemberByActivityGroupAndMember(activityGroup, member.getId())
            .map(GroupMember::isLeader)
            .orElseGet(member::isAdminRole);
    }

    // 해당 멤버가 특정 활동 그룹의 리더 또는 관리자인지 검증합니다.
    // 활동 멤버가 아닌 경우 false를 반환합니다.
    public boolean isMemberGroupLeaderRole(Long activityGroupId, String memberId) {
        ActivityGroup activityGroup = getActivityGroupById(activityGroupId);
        Member member = externalRetrieveMemberUseCase.getById(memberId);
        try {
            GroupMember groupMember = activityGroupMemberService.getGroupMemberByActivityGroupAndMember(activityGroup,
                member.getId());
            return groupMember.isLeader() || member.isAdminRole();
        } catch (BaseException e) {
            return false;
        }
    }

    public boolean isMemberHasRoleInActivityGroup(Member member, ActivityGroupRole role, Long activityGroupId) {
        List<GroupMember> groupMemberList = activityGroupMemberService.getGroupMemberByMemberId(member.getId());
        ActivityGroup activityGroup = activityGroupMemberService.getActivityGroupById(activityGroupId);
        return groupMemberList.stream()
            .anyMatch(groupMember -> groupMember.isSameRoleAndActivityGroup(role, activityGroup));
    }

    private void validateLeaderRoleChange(ActivityGroup activityGroup, GroupMember groupMember) {
        List<GroupMember> groupMembers = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(
            activityGroup.getId(), ActivityGroupRole.LEADER);
        if (groupMembers.size() == 1 && groupMember.isLeader()) {
            throw new BaseException(ErrorCode.SINGLE_LEADER_MODIFICATION_NOT_ALLOWED);
        }
    }

    public ActivityGroup validateAndGetActivityGroupForReporting(Long activityGroupId, Member member)
        throws IllegalAccessException {
        ActivityGroup activityGroup = getActivityGroupById(activityGroupId);
        if (!isMemberHasRoleInActivityGroup(member, ActivityGroupRole.LEADER, activityGroupId)) {
            throw new BaseException(ErrorCode.PERMISSION_DENIED, "해당 그룹의 리더만 보고서를 작성할 수 있습니다.");
        }
        if (!activityGroup.isProgressing()) {
            throw new IllegalAccessException("활동이 진행 중인 그룹이 아닙니다. 차시 보고서를 작성할 수 없습니다.");
        }
        return activityGroup;
    }

    private void validateLeaderOrAdminPermission(ActivityGroup activityGroup, Member member, String message) {
        if (!hasLeaderOrAdminRole(activityGroup, member)) {
            throw new BaseException(ErrorCode.PERMISSION_DENIED, message);
        }
    }

    private void validateMemberIsActive(GroupMember groupMember) {
        if (groupMember.isInactive()) {
            throw new BaseException(ErrorCode.INACTIVE_ACTIVITY_GROUP_MEMBER);
        }
    }

    private void validateNewPosition(GroupMember groupMember, ActivityGroupRole position) {
        if (position.isNone()) {
            throw new BaseException(ErrorCode.INVALID_ROLE_CHANGE, "직책이 없는 멤버로 변경할 수 없습니다.");
        }
        if (groupMember.isSameRole(position)) {
            throw new BaseException(ErrorCode.ALREADY_HAS_SAME_ACTIVITY_GROUP_ROLE);
        }
    }
}
