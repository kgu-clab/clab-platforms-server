package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.ActivityGroupRepository;
import page.clab.api.repository.GroupMemberRepository;
import page.clab.api.repository.GroupScheduleRepository;
import page.clab.api.type.dto.ActivityGroupDto;
import page.clab.api.type.dto.GroupScheduleDto;
import page.clab.api.type.entity.ActivityGroup;
import page.clab.api.type.entity.GroupMember;
import page.clab.api.type.entity.GroupSchedule;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.ActivityGroupRole;
import page.clab.api.type.etc.ActivityGroupStatus;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityGroupAdminService {

    private final MemberService memberService;

    private final ActivityGroupRepository activityGroupRepository;

    private final GroupMemberRepository groupMemberRepository;

    private final GroupScheduleRepository groupScheduleRepository;

    public List<ActivityGroupDto> getActivityGroupsByStatus(ActivityGroupStatus activityGroupStatus) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        List<ActivityGroup> activityGroupList = getActivityGroupByStatus(activityGroupStatus);
        return activityGroupList.stream()
                .map(ActivityGroupDto::of)
                .collect(Collectors.toList());
    }

    public void manageActivityGroup(Long activityGroupId, ActivityGroupStatus activityGroupStatus) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        activityGroup.setStatus(activityGroupStatus);
        activityGroupRepository.save(activityGroup);
    }

    @Transactional
    public void createActivityGroup(ActivityGroupDto activityGroupDto) {
        Member member = memberService.getCurrentMember();
        ActivityGroup activityGroup = ActivityGroup.of(activityGroupDto);
        activityGroup.setStatus(ActivityGroupStatus.승인대기);
        activityGroup.setProgress(0L);
        activityGroup.setCreatedAt(LocalDateTime.now());
        activityGroupRepository.save(activityGroup);
        GroupMember groupLeader = GroupMember.of(member, activityGroup);
        groupLeader.setRole(ActivityGroupRole.LEADER);
        groupMemberRepository.save(groupLeader);
    }

    public void updateActivityGroup(Long activityGroupId, ActivityGroupDto activityGroupDto) throws PermissionDeniedException {
        checkMemberGroupLeaderRole();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        activityGroup.setCategory(activityGroupDto.getCategory());
        activityGroup.setName(activityGroupDto.getName());
        activityGroup.setContent(activityGroupDto.getContent());
        activityGroup.setImageUrl(activityGroupDto.getImageUrl());
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

    public void createMemberAuthCode(Long activityGroupId, String code) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        GroupMember member = getGroupMemberByGroupIdAndRoleOrThrow(activityGroupId, ActivityGroupRole.LEADER);
        if (!member.getMember().getId().equals(currentMember.getId())) {
            throw new PermissionDeniedException("권한이 없습니다.");
        }
        activityGroup.setCode(code);
        activityGroupRepository.save(activityGroup);
    }

    public List<ActivityGroup> getActivityGroupByStatus(ActivityGroupStatus status) {
        return activityGroupRepository.findAllByStatus(status);
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
