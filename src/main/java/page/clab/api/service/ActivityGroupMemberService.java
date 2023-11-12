package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityGroupMemberService {

    private final ActivityGroupRepository activityGroupRepository;

    private final GroupScheduleRepository groupScheduleRepository;

    private final GroupMemberRepository groupMemberRepository;

    private final MemberService memberService;

    public List<ActivityGroupDto> getProjectList() {
        List<ActivityGroup> activityGroupList = activityGroupRepository.findAllByCategory("project");
        List<ActivityGroupDto> activityGroupDtoList = new ArrayList<>();
        for (ActivityGroup activityGroup : activityGroupList) {
            activityGroupDtoList.add(ActivityGroupDto.of(activityGroup));
        }
        return activityGroupDtoList;
    }

    public List<ActivityGroupDto> getStudyList() {
        List<ActivityGroup> activityGroupList = activityGroupRepository.findAllByCategory("study");
        List<ActivityGroupDto> activityGroupDtoList = new ArrayList<>();
        for (ActivityGroup activityGroup : activityGroupList) {
            activityGroupDtoList.add(ActivityGroupDto.of(activityGroup));
        }
        return activityGroupDtoList;
    }

    public ActivityGroupDto getProjectGroup(Long id) {
        ActivityGroup activityGroup = activityGroupRepository.findByIdAndCategory(id, "project").orElseThrow();
        return ActivityGroupDto.of(activityGroup);
    }

    public ActivityGroupDto getStudyGroup(Long id) {
        ActivityGroup activityGroup = activityGroupRepository.findByIdAndCategory(id, "study").orElseThrow();
        return ActivityGroupDto.of(activityGroup);
    }

    public List<GroupScheduleDto> getGroupScheduleList(Long id) {
        List<GroupSchedule> groupScheduleList = groupScheduleRepository.findAllByActivityGroupId(id);
        List<GroupScheduleDto> groupScheduleDtoList = new ArrayList<>();
        for (GroupSchedule groupSchedule : groupScheduleList) {
            groupScheduleDtoList.add(GroupScheduleDto.of(groupSchedule));
        }
        return groupScheduleDtoList;
    }

    public List<GroupMemberDto> getActivityGroupMemberList(Long id) {
        List<GroupMember> groupMemberList = groupMemberRepository.findAllByActivityGroupId(id);
        List<GroupMemberDto> groupMemberDtoList = new ArrayList<>();
        for (GroupMember groupMember : groupMemberList) {
            groupMemberDtoList.add(GroupMemberDto.of(groupMember));
        }
        return groupMemberDtoList;
    }

    public void authenticateActivityMember(Long id, String code) {
        ActivityGroup activityGroup = findById(id);
        Member member = memberService.getCurrentMember();
        if(!activityGroup.getCode().equals(code)) {
            throw new IllegalArgumentException("인증 코드가 일치하지 않습니다.");
        }
        GroupMember groupMember = GroupMember.of(member, activityGroup);
        groupMemberRepository.save(groupMember);
    }

    public ActivityGroup findById(Long id) {
        return activityGroupRepository.findById(id).orElseThrow();
    }

}
