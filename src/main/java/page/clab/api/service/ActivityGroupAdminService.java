package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.repository.ActivityGroupRepository;
import page.clab.api.repository.GroupMemberRepository;
import page.clab.api.repository.GroupScheduleRepository;
import page.clab.api.type.dto.ActivityGroupDto;
import page.clab.api.type.dto.GroupScheduleDto;
import page.clab.api.type.entity.ActivityGroup;
import page.clab.api.type.entity.GroupMember;
import page.clab.api.type.entity.GroupSchedule;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.ActivityGroupStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityGroupAdminService {

    private final ActivityGroupRepository activityGroupRepository;

    private final GroupMemberRepository groupMemberRepository;

    private final GroupScheduleRepository groupScheduleRepository;

    public List<ActivityGroupDto> getWaitingActivityGroup() {
        List<ActivityGroup> activityGroupList = activityGroupRepository.findAllByStatus(ActivityGroupStatus.승인대기);
        List<ActivityGroupDto> activityGroupDtoList = new ArrayList<>();
        for (ActivityGroup activityGroup : activityGroupList) {
            activityGroupDtoList.add(ActivityGroupDto.of(activityGroup));
        }
        return activityGroupDtoList;
    }
    public void approveActivityGroup(Long id) {
        ActivityGroup activityGroup = activityGroupRepository.findById(id).orElseThrow();
        activityGroup.setStatus(ActivityGroupStatus.활동중);
        activityGroupRepository.save(activityGroup);
    }

    public void completeActivityGroup(Long id) {
        ActivityGroup activityGroup = activityGroupRepository.findById(id).orElseThrow();
        activityGroup.setStatus(ActivityGroupStatus.활동종료);
        activityGroupRepository.save(activityGroup);
    }

    public void createActivityGroup(Member member, ActivityGroupDto activityGroupDto) {
        ActivityGroup activityGroup = ActivityGroup.of(activityGroupDto);
        activityGroup.setStatus(ActivityGroupStatus.승인대기);
        activityGroup.setProgress(0L);
        activityGroup.setCreatedAt(LocalDateTime.now());
        activityGroupRepository.save(activityGroup);

        GroupMember groupMember = GroupMember.of(member, activityGroup);
        groupMemberRepository.save(groupMember);
    }

    public void updateActivityGroup(Long id, ActivityGroupDto activityGroupDto) {
        ActivityGroup activityGroup = activityGroupRepository.findById(id).orElseThrow();
        activityGroup.setCategory(activityGroupDto.getCategory());
        activityGroup.setName(activityGroupDto.getName());
        activityGroup.setContent(activityGroupDto.getContent());
//        activityGroup.setImageUrl(activityGroupDto.getImageUrl());
        activityGroupRepository.save(activityGroup);
    }

    public void deleteActivityGroup(Long id) {
        ActivityGroup activityGroup = activityGroupRepository.findById(id).orElseThrow();
        activityGroupRepository.delete(activityGroup);
    }

    public void updateProjectProgress(Long id, int progress){
        ActivityGroup activityGroup = getActiveGroup(id);
        activityGroup.setProgress((long) progress);
        activityGroupRepository.save(activityGroup);
    }

    public void addSchedule(Long id, List<GroupScheduleDto> groupScheduleDto){
        ActivityGroup activityGroup = getActiveGroup(id);
        List<GroupSchedule> groupScheduleList = groupScheduleRepository.findAllByActivityGroupId(id);
        for (GroupScheduleDto dto : groupScheduleDto) {
            groupScheduleList.add(GroupSchedule.of(activityGroup, dto));
        }
    }



    public void createMemberAuthCode(Long id, String code){
        ActivityGroup activityGroup = getActiveGroup(id);
        activityGroup.setCode(code);
        activityGroupRepository.save(activityGroup);
    }

    public ActivityGroup getActiveGroup(Long id) {
        ActivityGroup activityGroup = activityGroupRepository.findById(id).orElseThrow();
        if (activityGroup.getStatus() != ActivityGroupStatus.활동중)
            throw new IllegalStateException();
        return activityGroup;
    }

}
