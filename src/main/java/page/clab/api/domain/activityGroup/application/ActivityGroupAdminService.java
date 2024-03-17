package page.clab.api.domain.activityGroup.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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
import page.clab.api.domain.activityGroup.exception.LeaderStatusChangeNotAllowedException;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityGroupAdminService {

    private final MemberService memberService;

    private final ActivityGroupMemberService activityGroupMemberService;

    private final ActivityGroupRepository activityGroupRepository;

    private final GroupScheduleRepository groupScheduleRepository;

    private final ApplyFormRepository applyFormRepository;

    private final NotificationService notificationService;

    @Transactional
    public Long createActivityGroup(ActivityGroupRequestDto activityGroupRequestDto) {
        Member member = memberService.getCurrentMember();
        ActivityGroup activityGroup = ActivityGroup.create(activityGroupRequestDto);
        activityGroupRepository.save(activityGroup);
        GroupMember groupLeader = GroupMember.create(member, activityGroup, ActivityGroupRole.LEADER, GroupMemberStatus.ACCEPTED);
        activityGroupMemberService.save(groupLeader);
        notificationService.sendNotificationToMember(
                member,
                "활동 그룹 생성이 완료되었습니다. 활동 승인이 완료되면 활동 그룹을 이용할 수 있습니다."
        );
        return activityGroup.getId();
    }

    public Long updateActivityGroup(Long activityGroupId, ActivityGroupUpdateRequestDto activityGroupUpdateRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        if (!isMemberGroupLeaderRole(activityGroup, member)) {
            throw new PermissionDeniedException("해당 활동을 수정할 권한이 없습니다.");
        }
        activityGroup.update(activityGroupUpdateRequestDto);
        return activityGroupRepository.save(activityGroup).getId();
    }

    public Long manageActivityGroup(Long activityGroupId, ActivityGroupStatus activityGroupStatus) {
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        activityGroup.setStatus(activityGroupStatus);
        Long id = activityGroupRepository.save(activityGroup).getId();
        GroupMember groupLeader = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(activityGroupId, ActivityGroupRole.LEADER);
        if (groupLeader != null) {
            notificationService.sendNotificationToMember(
                    groupLeader.getMember(),
                    "활동 그룹이 [" + activityGroupStatus.getDescription() + "] 상태로 변경되었습니다."
            );
        }
        return id;
    }

    @Transactional
    public Long deleteActivityGroup(Long activityGroupId) {
        List<GroupMember> groupMemberList = activityGroupMemberService.getGroupMemberByActivityGroupId(activityGroupId);
        List<GroupSchedule> groupScheduleList = groupScheduleRepository.findAllByActivityGroupIdOrderByIdDesc(activityGroupId);
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        GroupMember groupLeader = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(activityGroupId, ActivityGroupRole.LEADER);
        activityGroupMemberService.deleteAll(groupMemberList);
        groupScheduleRepository.deleteAll(groupScheduleList);
        activityGroupRepository.delete(activityGroup);
        if (groupLeader != null) {
            notificationService.sendNotificationToMember(
                    groupLeader.getMember(),
                    "활동 그룹 [" + activityGroup.getName() + "]이 삭제되었습니다."
            );
        }
        return activityGroup.getId();
    }

    public Long updateProjectProgress(Long activityGroupId, Long progress) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        if (!isMemberGroupLeaderRole(activityGroup, member)) {
            throw new PermissionDeniedException("해당 활동을 수정할 권한이 없습니다.");
        }
        activityGroup.setProgress(progress);
        return activityGroupRepository.save(activityGroup).getId();
    }

    @Transactional
    public Long addSchedule(Long activityGroupId, List<GroupScheduleDto> groupScheduleDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        if (!isMemberGroupLeaderRole(activityGroup, member)) {
            throw new PermissionDeniedException("해당 일정을 등록할 권한이 없습니다.");
        }
        groupScheduleDto.stream()
                .map(scheduleDto -> GroupSchedule.of(activityGroup, scheduleDto))
                .forEach(groupScheduleRepository::save);
        return activityGroup.getId();
    }

    public PagedResponseDto<ActivityGroupMemberWithApplyReasonResponseDto> getGroupMembersWithApplyReason(Long activityGroupId, Pageable pageable) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
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
        List<ActivityGroupMemberWithApplyReasonResponseDto> dtos = groupMembers.getContent().stream()
                .map(member -> {
                    String applyReason = memberIdToApplyReasonMap.getOrDefault(member.getMember().getId(), "");
                    return ActivityGroupMemberWithApplyReasonResponseDto.builder()
                            .memberId(member.getMember().getId())
                            .memberName(member.getMember().getName())
                            .role(member.getRole().toString())
                            .status(member.getStatus())
                            .applyReason(applyReason)
                            .build();
                }).toList();
        return new PagedResponseDto<>(new PageImpl<>(dtos, pageable, groupMembers.getTotalElements()));
    }

    public String manageGroupMemberStatus(Long activityGroupId, String memberId, GroupMemberStatus status) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        if (!isMemberGroupLeaderRole(activityGroup, currentMember)) {
            throw new PermissionDeniedException("해당 활동의 신청 멤버를 조회할 권한이 없습니다.");
        }
        Member member = memberService.getMemberByIdOrThrow(memberId);
        GroupMember groupMember = activityGroupMemberService.getGroupMemberByActivityGroupAndMemberOrThrow(activityGroup, member);
        if (groupMember.getRole() == ActivityGroupRole.LEADER) {
            throw new LeaderStatusChangeNotAllowedException("리더의 상태는 변경할 수 없습니다.");
        }
        groupMember.setStatus(status);
        if (status == GroupMemberStatus.ACCEPTED) {
            groupMember.setRole(ActivityGroupRole.MEMBER);
        } else {
            groupMember.setRole(null);
        }
        String id = activityGroupMemberService.save(groupMember).getMember().getId();

        notificationService.sendNotificationToMember(
                member,
                "활동 그룹 신청이 [" + status.getDescription() + "] 상태로 변경되었습니다."
        );
        return id;
    }

    public ActivityGroup getActivityGroupByIdOrThrow(Long activityGroupId) {
        return activityGroupRepository.findById(activityGroupId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 활동입니다."));
    }

    public boolean isActivityGroupProgressing(Long activityGroupId){
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        return activityGroup.getStatus() == ActivityGroupStatus.PROGRESSING;
    }

    public boolean isMemberGroupLeaderRole(ActivityGroup activityGroup, Member member) {
        GroupMember groupMember = activityGroupMemberService.getGroupMemberByActivityGroupAndMemberOrThrow(activityGroup, member);
        return groupMember.getRole() == ActivityGroupRole.LEADER && member.isAdminRole();
    }

    public boolean isMemberHasRoleInActivityGroup(Member member, ActivityGroupRole role, Long activityGroupId){
        List<GroupMember> groupMemberList = activityGroupMemberService.getGroupMemberByMember(member);
        ActivityGroup activityGroup = activityGroupMemberService.getActivityGroupByIdOrThrow(activityGroupId);

        return groupMemberList.stream()
                .anyMatch(groupMember ->
                        Objects.equals(groupMember.getActivityGroup().getId(), activityGroup.getId()) &&
                        groupMember.getRole() == role);
    }

}
