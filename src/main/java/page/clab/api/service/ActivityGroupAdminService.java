package page.clab.api.service;

import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.ActivityGroupRepository;
import page.clab.api.repository.GroupScheduleRepository;
import page.clab.api.type.dto.ActivityGroupRequestDto;
import page.clab.api.type.dto.ActivityGroupResponseDto;
import page.clab.api.type.dto.GroupMemberResponseDto;
import page.clab.api.type.dto.GroupScheduleDto;
import page.clab.api.type.dto.NotificationRequestDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.entity.ActivityGroup;
import page.clab.api.type.entity.GroupMember;
import page.clab.api.type.entity.GroupSchedule;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.ActivityGroupRole;
import page.clab.api.type.etc.ActivityGroupStatus;
import page.clab.api.type.etc.GroupMemberStatus;

@Service
@RequiredArgsConstructor
public class ActivityGroupAdminService {

    private final MemberService memberService;

    private final ActivityGroupMemberService activityGroupMemberService;

    private final ActivityGroupRepository activityGroupRepository;

    private final GroupScheduleRepository groupScheduleRepository;

    private final NotificationService notificationService;

    @Transactional
    public Long createActivityGroup(ActivityGroupRequestDto activityGroupRequestDto) {
        Member member = memberService.getCurrentMember();
        ActivityGroup activityGroup = ActivityGroup.of(activityGroupRequestDto);
        activityGroup.setStatus(ActivityGroupStatus.WAITING);
        activityGroup.setProgress(0L);
        Long id = activityGroupRepository.save(activityGroup).getId();
        GroupMember groupLeader = GroupMember.of(member, activityGroup);
        groupLeader.setRole(ActivityGroupRole.LEADER);
        groupLeader.setStatus(GroupMemberStatus.ACCEPTED);
        activityGroupMemberService.save(groupLeader);
        NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                .memberId(member.getId())
                .content("활동 그룹 생성이 완료되었습니다. 활동 승인이 완료되면 활동 그룹을 이용할 수 있습니다.")
                .build();
        notificationService.createNotification(notificationRequestDto);
        return id;
    }

    public PagedResponseDto<ActivityGroupResponseDto> getActivityGroupsByStatus(ActivityGroupStatus activityGroupStatus, Pageable pageable) {
        Page<ActivityGroup> activityGroupList = getActivityGroupByStatus(activityGroupStatus, pageable);
        return new PagedResponseDto<>(activityGroupList.map(ActivityGroupResponseDto::of));
    }

    public Long updateActivityGroup(Long activityGroupId, ActivityGroupRequestDto activityGroupRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        if (!isMemberGroupLeaderRole(member)) {
            throw new PermissionDeniedException("해당 활동을 수정할 권한이 없습니다.");
        }
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        activityGroup.setCategory(activityGroupRequestDto.getCategory());
        activityGroup.setName(activityGroupRequestDto.getName());
        activityGroup.setContent(activityGroupRequestDto.getContent());
        activityGroup.setImageUrl(activityGroupRequestDto.getImageUrl());
        return activityGroupRepository.save(activityGroup).getId();
    }

    public Long manageActivityGroup(Long activityGroupId, ActivityGroupStatus activityGroupStatus) {
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        activityGroup.setStatus(activityGroupStatus);
        Long id = activityGroupRepository.save(activityGroup).getId();
        GroupMember groupLeader = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(activityGroupId, ActivityGroupRole.LEADER);
        NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                .memberId(groupLeader.getMember().getId())
                .content("활동 그룹이 [" + activityGroupStatus.getDescription() + "] 상태로 변경되었습니다.")
                .build();
        notificationService.createNotification(notificationRequestDto);
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
        NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                .memberId(groupLeader.getMember().getId())
                .content("활동 그룹 [" + activityGroup.getName() + "]이 삭제되었습니다.")
                .build();
        notificationService.createNotification(notificationRequestDto);
        return activityGroup.getId();
    }

    public Long updateProjectProgress(Long activityGroupId, Long progress) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        if (!isMemberGroupLeaderRole(member)) {
            throw new PermissionDeniedException("해당 활동을 수정할 권한이 없습니다.");
        }
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        activityGroup.setProgress(progress);
        return activityGroupRepository.save(activityGroup).getId();
    }

    @Transactional
    public Long addSchedule(Long activityGroupId, List<GroupScheduleDto> groupScheduleDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        if (!isMemberGroupLeaderRole(member)) {
            throw new PermissionDeniedException("해당 일정을 등록할 권한이 없습니다.");
        }
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        groupScheduleDto.stream()
                .map(scheduleDto -> GroupSchedule.of(activityGroup, scheduleDto))
                .forEach(groupSchedule -> groupScheduleRepository.save(groupSchedule));
        return activityGroup.getId();
    }

    public PagedResponseDto<GroupMemberResponseDto> getApplyGroupMemberList(Long activityGroupId, Pageable pageable) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        if (!isMemberGroupLeaderRole(member)) {
            throw new PermissionDeniedException("해당 활동의 신청 멤버를 조회할 권한이 없습니다.");
        }
        Page<GroupMember> groupMemberList = activityGroupMemberService.getGroupMemberByActivityGroupIdAndStatus(activityGroupId, GroupMemberStatus.IN_PROGRESS, pageable);
        return new PagedResponseDto<>(groupMemberList.map(GroupMemberResponseDto::of));
    }

    public String manageGroupMemberStatus(String memberId, GroupMemberStatus status) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        if (!isMemberGroupLeaderRole(currentMember)) {
            throw new PermissionDeniedException("해당 활동의 신청 멤버를 조회할 권한이 없습니다.");
        }
        Member member = memberService.getMemberByIdOrThrow(memberId);
        GroupMember groupMember = activityGroupMemberService.getGroupMemberByMemberOrThrow(member);
        groupMember.setStatus(status);
        if (status == GroupMemberStatus.ACCEPTED) {
            groupMember.setRole(ActivityGroupRole.MEMBER);
        } else {
            groupMember.setRole(null);
        }
        String id = activityGroupMemberService.save(groupMember).getMember().getId();

        NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                .memberId(memberId)
                .content("활동 그룹 신청이 [" + status.getDescription() + "] 상태로 변경되었습니다.")
                .build();
        notificationService.createNotification(notificationRequestDto);
        return id;
    }

    public Page<ActivityGroup> getActivityGroupByStatus(ActivityGroupStatus status, Pageable pageable) {
        return activityGroupRepository.findAllByStatusOrderByCreatedAtDesc(status, pageable);
    }

    public ActivityGroup getActivityGroupByIdOrThrow(Long activityGroupId) {
        return activityGroupRepository.findById(activityGroupId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 활동입니다."));
    }

    public boolean isMemberGroupLeaderRole(Member member) {
        GroupMember groupMember = activityGroupMemberService.getGroupMemberByMemberOrThrow(member);
        if (groupMember.getRole() != ActivityGroupRole.LEADER || !memberService.isMemberAdminRole(member)) {
            return false;
        }
        return true;
    }

    public boolean isMemberHasRoleInActivityGroup(Member member, ActivityGroupRole role ,Long activityGroupId){
        List<GroupMember> groupMemberList = activityGroupMemberService.getGroupMemberByMember(member);
        ActivityGroup activityGroup = activityGroupMemberService.getActivityGroupByIdOrThrow(activityGroupId);

        return groupMemberList.stream()
                .anyMatch(groupMember ->
                        groupMember.getActivityGroup().getId() == activityGroup.getId() &&
                        groupMember.getRole() == role);
    }

}
