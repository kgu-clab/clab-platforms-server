package page.clab.api.domain.activityGroup.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activityGroup.dao.ActivityGroupRepository;
import page.clab.api.domain.activityGroup.dao.ApplyFormRepository;
import page.clab.api.domain.activityGroup.dao.GroupScheduleRepository;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupRole;
import page.clab.api.domain.activityGroup.domain.ActivityGroupStatus;
import page.clab.api.domain.activityGroup.domain.ApplyForm;
import page.clab.api.domain.activityGroup.domain.GroupMember;
import page.clab.api.domain.activityGroup.domain.GroupMemberStatus;
import page.clab.api.domain.activityGroup.domain.GroupSchedule;
import page.clab.api.domain.activityGroup.dto.param.GroupScheduleDto;
import page.clab.api.domain.activityGroup.dto.request.ActivityGroupRequestDto;
import page.clab.api.domain.activityGroup.dto.request.ActivityGroupUpdateRequestDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupMemberWithApplyReasonResponseDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupResponseDto;
import page.clab.api.domain.member.application.port.in.MemberRetrievalUseCase;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.application.port.in.NotificationSenderUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityGroupAdminService {

    private final MemberRetrievalUseCase memberRetrievalUseCase;
    private final ActivityGroupMemberService activityGroupMemberService;
    private final NotificationSenderUseCase notificationService;
    private final ValidationService validationService;
    private final ActivityGroupRepository activityGroupRepository;
    private final GroupScheduleRepository groupScheduleRepository;
    private final ApplyFormRepository applyFormRepository;

    @Transactional
    public Long createActivityGroup(ActivityGroupRequestDto requestDto) {
        Member currentMember = memberRetrievalUseCase.getCurrentMember();
        ActivityGroup activityGroup = ActivityGroupRequestDto.toEntity(requestDto);
        validationService.checkValid(activityGroup);
        activityGroupRepository.save(activityGroup);

        GroupMember groupLeader = GroupMember.create(currentMember, activityGroup, ActivityGroupRole.LEADER, GroupMemberStatus.ACCEPTED);
        validationService.checkValid(groupLeader);
        activityGroupMemberService.save(groupLeader);

        notificationService.sendNotificationToMember(currentMember.getId(), "활동 그룹 생성이 완료되었습니다. 활동 승인이 완료되면 활동 그룹을 이용할 수 있습니다.");
        return activityGroup.getId();
    }

    @Transactional
    public Long updateActivityGroup(Long activityGroupId, ActivityGroupUpdateRequestDto requestDto) throws PermissionDeniedException {
        Member currentMember = memberRetrievalUseCase.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        if (!isMemberGroupLeaderRole(activityGroup, currentMember)) {
            throw new PermissionDeniedException("해당 활동을 수정할 권한이 없습니다.");
        }
        activityGroup.update(requestDto);
        validationService.checkValid(activityGroup);
        return activityGroupRepository.save(activityGroup).getId();
    }

    @Transactional
    public Long manageActivityGroup(Long activityGroupId, ActivityGroupStatus status) {
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        activityGroup.updateStatus(status);
        validationService.checkValid(activityGroup);
        activityGroupRepository.save(activityGroup);

        GroupMember groupLeader = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(activityGroupId, ActivityGroupRole.LEADER);
        if (groupLeader != null) {
            notificationService.sendNotificationToMember(groupLeader.getMember().getId(), "활동 그룹이 [" + status.getDescription() + "] 상태로 변경되었습니다.");
        }
        return activityGroup.getId();
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
            notificationService.sendNotificationToMember(groupLeader.getMember().getId(), "활동 그룹 [" + activityGroup.getName() + "]이 삭제되었습니다.");
        }
        return activityGroup.getId();
    }

    @Transactional
    public Long updateProjectProgress(Long activityGroupId, Long progress) throws PermissionDeniedException {
        Member currentMember = memberRetrievalUseCase.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        if (!isMemberGroupLeaderRole(activityGroup, currentMember)) {
            throw new PermissionDeniedException("해당 활동을 수정할 권한이 없습니다.");
        }
        activityGroup.updateProgress(progress);
        validationService.checkValid(activityGroup);
        return activityGroupRepository.save(activityGroup).getId();
    }

    @Transactional
    public Long addSchedule(Long activityGroupId, List<GroupScheduleDto> scheduleDtos) throws PermissionDeniedException {
        Member currentMember = memberRetrievalUseCase.getCurrentMember();
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
        Member currentMember = memberRetrievalUseCase.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        if (!(isMemberGroupLeaderRole(activityGroup, currentMember) || currentMember.isAdminRole())) {
            throw new PermissionDeniedException("해당 활동의 멤버를 조회할 권한이 없습니다.");
        }

        List<ApplyForm> applyForms = applyFormRepository.findAllByActivityGroup(activityGroup);
        Map<String, String> memberIdToApplyReasonMap = applyForms.stream()
                .collect(Collectors.toMap(
                        applyForm -> applyForm.getMember().getId(),
                        ApplyForm::getApplyReason
                ));

        Page<GroupMember> groupMembers = activityGroupMemberService.getGroupMemberByActivityGroupId(activityGroupId, pageable);
        List<ActivityGroupMemberWithApplyReasonResponseDto> groupMembersWithApplyReason = groupMembers.getContent().stream()
                .map(groupMember -> {
                    String applyReason = memberIdToApplyReasonMap.getOrDefault(groupMember.getMember().getId(), "");
                    return ActivityGroupMemberWithApplyReasonResponseDto.create(groupMember, applyReason);
                }).toList();

        return new PagedResponseDto<>(new PageImpl<>(groupMembersWithApplyReason, pageable, groupMembers.getTotalElements()));
    }

    @Transactional
    public String manageGroupMemberStatus(Long activityGroupId, String memberId, GroupMemberStatus status) throws PermissionDeniedException {
        Member currentMember = memberRetrievalUseCase.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        if (!isMemberGroupLeaderRole(activityGroup, currentMember)) {
            throw new PermissionDeniedException("해당 활동의 신청 멤버를 조회할 권한이 없습니다.");
        }

        Member member = memberRetrievalUseCase.findByIdOrThrow(memberId);
        GroupMember groupMember = activityGroupMemberService.getGroupMemberByActivityGroupAndMemberOrThrow(activityGroup, member);
        groupMember.validateAccessPermission();
        groupMember.updateStatus(status);
        activityGroupMemberService.save(groupMember);

        notificationService.sendNotificationToMember(member.getId(), "활동 그룹 신청이 [" + status.getDescription() + "] 상태로 변경되었습니다.");
        return groupMember.getMember().getId();
    }

    public ActivityGroup getActivityGroupByIdOrThrow(Long activityGroupId) {
        return activityGroupRepository.findById(activityGroupId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 활동입니다."));
    }

    public boolean isMemberGroupLeaderRole(ActivityGroup activityGroup, Member member) {
        GroupMember groupMember = activityGroupMemberService.getGroupMemberByActivityGroupAndMemberOrThrow(activityGroup, member);
        return groupMember.isLeader() && member.isAdminRole();
    }

    public boolean isMemberHasRoleInActivityGroup(Member member, ActivityGroupRole role, Long activityGroupId) {
        List<GroupMember> groupMemberList = activityGroupMemberService.getGroupMemberByMember(member);
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

}
