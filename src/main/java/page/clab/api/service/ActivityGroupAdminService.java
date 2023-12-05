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
        return activityGroupRepository.save(activityGroup).getId();
    }

    @Transactional
    public Long deleteActivityGroup(Long activityGroupId) {
        List<GroupMember> groupMemberList = activityGroupMemberService.getGroupMemberByActivityGroupId(activityGroupId);
        List<GroupSchedule> groupScheduleList = groupScheduleRepository.findAllByActivityGroupIdOrderByIdDesc(activityGroupId);
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        activityGroupMemberService.deleteAll(groupMemberList);
        groupScheduleRepository.deleteAll(groupScheduleList);
        activityGroupRepository.delete(activityGroup);
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
            throw new PermissionDeniedException("해당 활동의 신청 멤버 상태를 변경할 권한이 없습니다.");
        }
        Member member = memberService.getMemberByIdOrThrow(memberId);
        GroupMember groupMember = activityGroupMemberService.getGroupMemberByMemberOrThrow(member);
        groupMember.setStatus(status);
        if (status == GroupMemberStatus.ACCEPTED) {
            groupMember.setRole(ActivityGroupRole.MEMBER);
        }else {
            groupMember.setRole(null);
        }
        return activityGroupMemberService.save(groupMember).getMember().getId();
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

}
