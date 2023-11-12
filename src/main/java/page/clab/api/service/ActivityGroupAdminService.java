package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityGroupAdminService {

    private final MemberService memberService;

    private final ActivityGroupRepository activityGroupRepository;

    private final GroupMemberRepository groupMemberRepository;

    private final GroupScheduleRepository groupScheduleRepository;

    //
    public List<ActivityGroupDto> getWaitingActivityGroup() throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        List<ActivityGroup> activityGroupList = findAllByStatus(ActivityGroupStatus.승인대기);
        List<ActivityGroupDto> activityGroupDtoList = new ArrayList<>();
        for (ActivityGroup activityGroup : activityGroupList) {
            activityGroupDtoList.add(ActivityGroupDto.of(activityGroup));
        }
        return activityGroupDtoList;
    }
    public void approveActivityGroup(Long id) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        ActivityGroup activityGroup = findGroupById(id);
        activityGroup.setStatus(ActivityGroupStatus.활동중);
        activityGroupRepository.save(activityGroup);
    }

    public void completeActivityGroup(Long id) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        ActivityGroup activityGroup = findGroupById(id);
        activityGroup.setStatus(ActivityGroupStatus.활동종료);
        activityGroupRepository.save(activityGroup);
    }

    public void createActivityGroup(ActivityGroupDto activityGroupDto) {
        Member member = memberService.getCurrentMember();
        ActivityGroup activityGroup = ActivityGroup.of(activityGroupDto);
        activityGroup.setStatus(ActivityGroupStatus.승인대기);
        activityGroup.setProgress(0L);
        activityGroup.setCreatedAt(LocalDateTime.now());
        activityGroupRepository.save(activityGroup);

        GroupMember groupLeader = GroupMember.of(member, activityGroup);
        groupMemberRepository.save(groupLeader);
    }

    public void updateActivityGroup(Long id, ActivityGroupDto activityGroupDto) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        ActivityGroup activityGroup = findGroupById(id);
        activityGroup.setCategory(activityGroupDto.getCategory());
        activityGroup.setName(activityGroupDto.getName());
        activityGroup.setContent(activityGroupDto.getContent());
        activityGroup.setImageUrl(activityGroupDto.getImageUrl());
        activityGroupRepository.save(activityGroup);
    }

    public void deleteActivityGroup(Long id) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        ActivityGroup activityGroup = findGroupById(id);
        activityGroupRepository.delete(activityGroup);

        List<GroupMember> groupMemberList = groupMemberRepository.findAllByActivityGroupId(id);
        groupMemberRepository.deleteAll(groupMemberList);
    }

    public void updateProjectProgress(Long id, Long progress) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        ActivityGroup activityGroup = findGroupById(id);
        activityGroup.setProgress(progress);
        activityGroupRepository.save(activityGroup);
    }

    public void addSchedule(Long id, List<GroupScheduleDto> groupScheduleDto) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        ActivityGroup activityGroup = findGroupById(id);
        GroupSchedule groupSchedule;
        for (GroupScheduleDto scheduleDto : groupScheduleDto) {
            groupSchedule = GroupSchedule.of(activityGroup, scheduleDto);
            groupScheduleRepository.save(groupSchedule);
        }
    }

    public void createMemberAuthCode(Long id, String code) throws PermissionDeniedException {
        ActivityGroup activityGroup = findGroupById(id);
        GroupMember member = groupMemberRepository.findByActivityGroupIdAndRole(id, ActivityGroupRole.LEADER).orElseThrow();
        if (!member.getMember().getId().equals(memberService.getCurrentMember().getId())) {
            throw new PermissionDeniedException("권한이 없습니다.");
        }
        activityGroup.setCode(code);
        activityGroupRepository.save(activityGroup);
    }

    public ActivityGroup findGroupById(Long id) {
        return activityGroupRepository.findById(id).orElseThrow();
    }

    public List<ActivityGroup> findAllByStatus(ActivityGroupStatus status) {
        return activityGroupRepository.findAllByStatus(status);
    }

}
