package page.clab.api.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.repository.ActivityGroupRepository;
import page.clab.api.repository.GroupMemberRepository;
import page.clab.api.repository.GroupScheduleRepository;
import page.clab.api.type.dto.ActivityGroupDto;
import page.clab.api.type.dto.GroupMemberDto;
import page.clab.api.type.dto.GroupScheduleDto;
import page.clab.api.type.entity.ActivityGroup;
import page.clab.api.type.entity.GroupMember;
import page.clab.api.type.entity.GroupSchedule;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.ActivityGroupRole;

@Service
@RequiredArgsConstructor
public class ActivityGroupMemberService {

    private final ActivityGroupRepository activityGroupRepository;

    private final GroupScheduleRepository groupScheduleRepository;

    private final GroupMemberRepository groupMemberRepository;

    private final MemberService memberService;

    public List<ActivityGroupDto> getActivityGroups(String category) {
        List<ActivityGroup> activityGroupList = getActivityGroupByCategory(category);
        return activityGroupList.stream()
                .map(ActivityGroupDto::of)
                .collect(Collectors.toList());
    }

    public ActivityGroupDto getActivityGroup(Long activityGroupId) {
    ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        return ActivityGroupDto.of(activityGroup);
    }

    public List<GroupScheduleDto> getGroupSchedules(Long activityGroupId) {
        List<GroupSchedule> groupSchedules = getGroupScheduleByActivityGroupId(activityGroupId);
        return groupSchedules.stream()
                .map(GroupScheduleDto::of)
                .collect(Collectors.toList());
    }

    public List<GroupMemberDto> getActivityGroupMembers(Long activityGroupId) {
        List<GroupMember> groupMembers = getGroupMemberByActivityGroupId(activityGroupId);
        return groupMembers.stream()
                .map(GroupMemberDto::of)
                .collect(Collectors.toList());
    }
    
    public void authenticateActivityMember(Long activityGroupId, String code) {
        Member member = memberService.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        if(!activityGroup.getCode().equals(code)) {
            throw new IllegalArgumentException("인증 코드가 일치하지 않습니다.");
        }
        GroupMember groupMember = GroupMember.of(member, activityGroup);
        groupMember.setRole(ActivityGroupRole.MEMBER);
        groupMemberRepository.save(groupMember);
    }

    public ActivityGroup getActivityGroupByIdOrThrow(Long activityGroupId) {
        return activityGroupRepository.findById(activityGroupId)
                .orElseThrow(() -> new NotFoundException("해당 활동이 존재하지 않습니다."));
    }

    private List<ActivityGroup> getActivityGroupByCategory(String category) {
        return activityGroupRepository.findAllByCategory(category);
    }

    private List<GroupSchedule> getGroupScheduleByActivityGroupId(Long activityGroupId) {
        return groupScheduleRepository.findAllByActivityGroupId(activityGroupId);
    }

    private List<GroupMember> getGroupMemberByActivityGroupId(Long activityGroupId) {
        return groupMemberRepository.findAllByActivityGroupId(activityGroupId);
    }

}
