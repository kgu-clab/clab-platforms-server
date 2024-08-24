package page.clab.api.domain.activity.activitygroup.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import page.clab.api.domain.activity.activitygroup.dto.param.GroupScheduleDto;
import page.clab.api.domain.activity.activitygroup.dto.request.ActivityGroupRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.request.ActivityGroupUpdateRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupBoardStatusUpdatedResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupMemberWithApplyReasonResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupResponseDto;
import page.clab.api.domain.activity.activitygroup.exception.DuplicateRoleException;
import page.clab.api.domain.activity.activitygroup.exception.InactiveMemberException;
import page.clab.api.domain.activity.activitygroup.exception.InvalidRoleException;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.external.memberManagement.notification.application.port.ExternalSendNotificationUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityGroupAdminService {

    private final ActivityGroupMemberService activityGroupMemberService;
    private final ActivityGroupRepository activityGroupRepository;
    private final GroupScheduleRepository groupScheduleRepository;
    private final ApplyFormRepository applyFormRepository;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalSendNotificationUseCase externalSendNotificationUseCase;

    @Transactional
    public Long createActivityGroup(ActivityGroupRequestDto requestDto) {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = ActivityGroupRequestDto.toEntity(requestDto);
        activityGroup.validateContentLength();
        activityGroup.validateAndSetGithubUrl(activityGroup.getGithubUrl());
        activityGroupRepository.save(activityGroup);

        GroupMember groupLeader = GroupMember.create(currentMember.getId(), activityGroup, ActivityGroupRole.LEADER, GroupMemberStatus.ACCEPTED);
        activityGroupMemberService.save(groupLeader);

        externalSendNotificationUseCase.sendNotificationToMember(currentMember.getId(), "활동 그룹 생성이 완료되었습니다. 활동 승인이 완료되면 활동 그룹을 이용할 수 있습니다.");
        return activityGroup.getId();
    }

    @Transactional
    public Long updateActivityGroup(Long activityGroupId, ActivityGroupUpdateRequestDto requestDto) throws PermissionDeniedException {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        if (!isMemberGroupLeaderRole(activityGroup, currentMember)) {
            throw new PermissionDeniedException("해당 활동을 수정할 권한이 없습니다.");
        }
        activityGroup.update(requestDto);
        activityGroup.validateContentLength();
        return activityGroupRepository.save(activityGroup).getId();
    }

    @Transactional
    public ActivityGroupBoardStatusUpdatedResponseDto manageActivityGroup(Long activityGroupId, ActivityGroupStatus status) {
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        activityGroup.updateStatus(status);
        activityGroupRepository.save(activityGroup);

        GroupMember groupLeader = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(activityGroupId, ActivityGroupRole.LEADER);
        if (groupLeader != null) {
            externalSendNotificationUseCase.sendNotificationToMember(groupLeader.getMemberId(), "활동 그룹이 [" + status.getDescription() + "] 상태로 변경되었습니다.");
        }
        return ActivityGroupBoardStatusUpdatedResponseDto.toDto(activityGroupId, status);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupResponseDto> getDeletedActivityGroups(Pageable pageable) {
        Page<ActivityGroup> activityGroups = activityGroupRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(activityGroups.map(ActivityGroupResponseDto::toDto));
    }

    @Transactional
    public Long deleteActivityGroup(Long activityGroupId) {
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        List<GroupMember> groupMembers = activityGroupMemberService.getGroupMemberByActivityGroupId(activityGroupId);
        List<GroupSchedule> groupSchedules = groupScheduleRepository.findAllByActivityGroupIdOrderByIdDesc(activityGroupId);
        GroupMember groupLeader = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(activityGroupId, ActivityGroupRole.LEADER);

        activityGroupMemberService.deleteAll(groupMembers);
        groupScheduleRepository.deleteAll(groupSchedules);
        activityGroupRepository.delete(activityGroup);

        if (groupLeader != null) {
            externalSendNotificationUseCase.sendNotificationToMember(groupLeader.getMemberId(), "활동 그룹 [" + activityGroup.getName() + "]이 삭제되었습니다.");
        }
        return activityGroup.getId();
    }

    @Transactional
    public Long updateProjectProgress(Long activityGroupId, Long progress) throws PermissionDeniedException {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        if (!isMemberGroupLeaderRole(activityGroup, currentMember)) {
            throw new PermissionDeniedException("해당 활동을 수정할 권한이 없습니다.");
        }
        activityGroup.updateProgress(progress);
        return activityGroupRepository.save(activityGroup).getId();
    }

    @Transactional
    public Long addSchedule(Long activityGroupId, List<GroupScheduleDto> scheduleDtos) throws PermissionDeniedException {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        if (!isMemberGroupLeaderRole(activityGroup, currentMember)) {
            throw new PermissionDeniedException("해당 일정을 등록할 권한이 없습니다.");
        }
        List<GroupSchedule> groupSchedules = scheduleDtos.stream()
                .map(scheduleDto -> GroupScheduleDto.toEntity(scheduleDto, activityGroup))
                .toList();
        groupScheduleRepository.saveAll(groupSchedules);
        return activityGroup.getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupMemberWithApplyReasonResponseDto> getGroupMembersWithApplyReason(Long activityGroupId, Pageable pageable) throws PermissionDeniedException {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        if (!(isMemberGroupLeaderRole(activityGroup, currentMember) || currentMember.isAdminRole())) {
            throw new PermissionDeniedException("해당 활동의 멤버를 조회할 권한이 없습니다.");
        }

        List<ApplyForm> applyForms = applyFormRepository.findAllByActivityGroup(activityGroup);
        Map<String, String> memberIdToApplyReasonMap = applyForms.stream()
                .collect(Collectors.toMap(
                        ApplyForm::getMemberId,
                        ApplyForm::getApplyReason
                ));

        Page<GroupMember> groupMembers = activityGroupMemberService.getGroupMemberByActivityGroupId(activityGroupId, pageable);
        List<ActivityGroupMemberWithApplyReasonResponseDto> groupMembersWithApplyReason = groupMembers.getContent().stream()
                .map(groupMember -> {
                    String applyReason = memberIdToApplyReasonMap.getOrDefault(groupMember.getMemberId(), "");
                    Member member = externalRetrieveMemberUseCase.findByIdOrThrow(groupMember.getMemberId());
                    return ActivityGroupMemberWithApplyReasonResponseDto.create(member, groupMember, applyReason);
                }).toList();

        return new PagedResponseDto<>(new PageImpl<>(groupMembersWithApplyReason, pageable, groupMembers.getTotalElements()));
    }

    @Transactional
    public Long manageGroupMemberStatus(Long activityGroupId, String memberId, GroupMemberStatus status) throws PermissionDeniedException {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        if (!isMemberGroupLeaderRole(activityGroup, currentMember)) {
            throw new PermissionDeniedException("해당 활동의 신청 멤버를 조회할 권한이 없습니다.");
        }

        Member member = externalRetrieveMemberUseCase.findByIdOrThrow(memberId);
        GroupMember groupMember = activityGroupMemberService.getGroupMemberByActivityGroupAndMemberOrThrow(activityGroup, member.getId());
        groupMember.validateAccessPermission();
        groupMember.updateStatus(status);
        activityGroupMemberService.save(groupMember);

        externalSendNotificationUseCase.sendNotificationToMember(member.getId(), "활동 그룹 신청이 [" + status.getDescription() + "] 상태로 변경되었습니다.");
        return activityGroup.getId();
    }

    @Transactional
    public Long changeGroupMemberPosition(Long activityGroupId, String memberId, ActivityGroupRole position) throws PermissionDeniedException {
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        GroupMember groupMember = activityGroupMemberService.getGroupMemberByActivityGroupAndMemberOrThrow(activityGroup, memberId);
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();

        validateLeaderPermission(activityGroup, currentMember);
        validateMemberIsActive(groupMember);
        validateNewPosition(groupMember, position);

        groupMember.updateRole(position);
        activityGroupMemberService.save(groupMember);

        return activityGroup.getId();
    }

    public ActivityGroup getActivityGroupByIdOrThrow(Long activityGroupId) {
        return activityGroupRepository.findById(activityGroupId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 활동입니다."));
    }

    public boolean isMemberGroupLeaderRole(ActivityGroup activityGroup, Member member) {
        GroupMember groupMember = activityGroupMemberService.getGroupMemberByActivityGroupAndMemberOrThrow(activityGroup, member.getId());
        return groupMember.isLeader() && member.isAdminRole();
    }

    public boolean isMemberHasRoleInActivityGroup(Member member, ActivityGroupRole role, Long activityGroupId) {
        List<GroupMember> groupMemberList = activityGroupMemberService.getGroupMemberByMemberId(member.getId());
        ActivityGroup activityGroup = activityGroupMemberService.getActivityGroupByIdOrThrow(activityGroupId);
        return groupMemberList.stream()
                .anyMatch(groupMember -> groupMember.isSameRoleAndActivityGroup(role, activityGroup));
    }

    public ActivityGroup validateAndGetActivityGroupForReporting(Long activityGroupId, Member member) throws PermissionDeniedException, IllegalAccessException {
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        if (!isMemberHasRoleInActivityGroup(member, ActivityGroupRole.LEADER, activityGroupId)) {
            throw new PermissionDeniedException("해당 그룹의 리더만 보고서를 작성할 수 있습니다.");
        }
        if (!activityGroup.isProgressing()) {
            throw new IllegalAccessException("활동이 진행 중인 그룹이 아닙니다. 차시 보고서를 작성할 수 없습니다.");
        }
        return activityGroup;
    }

    private void validateLeaderPermission(ActivityGroup activityGroup, Member member) throws PermissionDeniedException {
        if (!isMemberGroupLeaderRole(activityGroup, member)) {
            throw new PermissionDeniedException("해당 활동의 멤버 직책을 변경할 권한이 없습니다.");
        }
    }

    private void validateMemberIsActive(GroupMember groupMember) {
        if (groupMember.isInactive()) {
            throw new InactiveMemberException("활동에 참여하지 않은 멤버의 직책을 변경할 수 없습니다.");
        }
    }

    private void validateNewPosition(GroupMember groupMember, ActivityGroupRole position) {
        if (position.isNone()) {
            throw new InvalidRoleException("직책이 없는 멤버로 변경할 수 없습니다.");
        }
        if (groupMember.isSameRole(position)) {
            throw new DuplicateRoleException("이미 해당 직책을 가지고 있습니다.");
        }
    }
}
