package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.ActivityGroupRepository;
import page.clab.api.repository.GroupMemberRepository;
import page.clab.api.repository.GroupScheduleRepository;
import page.clab.api.type.dto.ActivityGroupRequestDto;
import page.clab.api.type.dto.ActivityGroupResponseDto;
import page.clab.api.type.dto.GroupScheduleDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.entity.ActivityGroup;
import page.clab.api.type.entity.GroupMember;
import page.clab.api.type.entity.GroupSchedule;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.ActivityGroupCategory;
import page.clab.api.type.etc.ActivityGroupRole;
import page.clab.api.type.etc.ActivityGroupStatus;
import page.clab.api.type.etc.GroupMemberStatus;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityGroupAdminService {

    private final MemberService memberService;

    private final ActivityGroupRepository activityGroupRepository;

    private final GroupMemberRepository groupMemberRepository;

    private final GroupScheduleRepository groupScheduleRepository;

    public PagedResponseDto<ActivityGroupResponseDto> getActivityGroupsByStatus(ActivityGroupStatus activityGroupStatus, Pageable pageable) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        Page<ActivityGroup> activityGroupList = getActivityGroupByStatus(activityGroupStatus, pageable);
        return new PagedResponseDto<>(activityGroupList.map(ActivityGroupResponseDto::of));
    }

    public void manageActivityGroup(Long activityGroupId, ActivityGroupStatus activityGroupStatus) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        activityGroup.setStatus(activityGroupStatus);
        activityGroupRepository.save(activityGroup);
    }

    @Transactional
    public void createActivityGroup(ActivityGroupCategory category, ActivityGroupRequestDto activityGroupRequestDto) {
        Member member = memberService.getCurrentMember();
        ActivityGroup activityGroup = ActivityGroup.of(activityGroupRequestDto);
        activityGroup.setCategory(category);
        activityGroup.setStatus(ActivityGroupStatus.WAITING);
        activityGroup.setProgress(0L);
        activityGroup.setCreatedAt(LocalDateTime.now());
        activityGroupRepository.save(activityGroup);
        GroupMember groupLeader = GroupMember.of(member, activityGroup);
        groupLeader.setRole(ActivityGroupRole.LEADER);
        groupLeader.setStatus(GroupMemberStatus.ACCEPTED);
        groupMemberRepository.save(groupLeader);
    }

    public void updateActivityGroup(Long activityGroupId, ActivityGroupRequestDto activityGroupRequestDto) throws PermissionDeniedException {
        checkMemberGroupLeaderRole();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        activityGroup.setName(activityGroupRequestDto.getName());
        activityGroup.setContent(activityGroupRequestDto.getContent());
        activityGroup.setImageUrl(activityGroupRequestDto.getImageUrl());
        activityGroupRepository.save(activityGroup);
    }

    @Transactional
    public void deleteActivityGroup(Long activityGroupId) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        List<GroupMember> groupMemberList = groupMemberRepository.findAllByActivityGroupId(activityGroupId);
        groupMemberList.stream()
                .forEach(groupMember -> groupMemberRepository.delete(groupMember));
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        activityGroupRepository.delete(activityGroup);
    }

    public void updateProjectProgress(Long activityGroupId, Long progress) throws PermissionDeniedException {
        checkMemberGroupLeaderRole();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        activityGroup.setProgress(progress);
        activityGroupRepository.save(activityGroup);
    }

    @Transactional
    public void addSchedule(Long activityGroupId, List<GroupScheduleDto> groupScheduleDto) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        groupScheduleDto.stream()
                .map(scheduleDto -> GroupSchedule.of(activityGroup, scheduleDto))
                .forEach(groupSchedule -> groupScheduleRepository.save(groupSchedule));
    }

//    public List<MemberResponseDto> getApplyGroupMemberList (Long activityGroupId) throws PermissionDeniedException {
//        checkMemberGroupLeaderRole();
//        List<GroupMember> groupMemberList = groupMemberRepository.findAllByActivityGroupIdAndStatus(activityGroupId, GroupMemberStatus.IN_PROGRESS);
//        return groupMemberList.stream()
//                .map(groupMember -> MemberResponseDto.of(groupMember.getMember()))
//                .collect(Collectors.toList());
//    }
//
//    public void manageGroupMemberStatus(String memberId, GroupMemberStatus status) throws PermissionDeniedException {
//        checkMemberGroupLeaderRole();
//        Member member = memberService.getMemberByIdOrThrow(memberId);
//        GroupMember groupMember = getGroupMemberByMemberOrThrow(member);
//        groupMember.setStatus(status);
//        if (status == GroupMemberStatus.ACCEPTED) {
//            groupMember.setRole(ActivityGroupRole.MEMBER);
//        }
//        groupMemberRepository.save(groupMember);
//    }

    public Page<ActivityGroup> getActivityGroupByStatus(ActivityGroupStatus status, Pageable pageable) {
        return activityGroupRepository.findAllByStatusOrderByCreatedAtDesc(status, pageable);
    }

    public ActivityGroup getActivityGroupByIdOrThrow(Long id) {
        return activityGroupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 활동입니다."));
    }

    private GroupMember getGroupMemberByGroupIdAndRoleOrThrow(Long activityGroupId, ActivityGroupRole activityGroupRole) {
        return groupMemberRepository.findByActivityGroupIdAndRole(activityGroupId, activityGroupRole)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 멤버입니다."));
    }

    private GroupMember getGroupMemberByMemberOrThrow(Member member) {
        return groupMemberRepository.findByMember(member)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 멤버입니다."));
    }

    public void checkMemberGroupLeaderRole() throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        GroupMember groupMember = getGroupMemberByMemberOrThrow(member);
        if (groupMember.getRole() != ActivityGroupRole.LEADER || !memberService.isMemberAdminRole(member)) {
            throw new PermissionDeniedException("권한이 부족합니다.");
        }
    }

}
