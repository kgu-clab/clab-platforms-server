package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.repository.ActivityGroupRepository;
import page.clab.api.repository.GroupMemberRepository;
import page.clab.api.repository.GroupScheduleRepository;
import page.clab.api.type.dto.ActivityGroupDetailResponseDto;
import page.clab.api.type.dto.ActivityGroupResponseDto;
import page.clab.api.type.dto.GroupMemberDto;
import page.clab.api.type.dto.GroupScheduleDto;
import page.clab.api.type.entity.ActivityGroup;
import page.clab.api.type.entity.GroupMember;
import page.clab.api.type.entity.GroupSchedule;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.ActivityGroupCategory;
import page.clab.api.type.etc.ActivityGroupRole;
import page.clab.api.type.etc.ActivityGroupStatus;
import page.clab.api.type.etc.GroupMemberStatus;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityGroupMemberService {

    private final ActivityGroupRepository activityGroupRepository;

    private final GroupScheduleRepository groupScheduleRepository;

    private final GroupMemberRepository groupMemberRepository;

    private final MemberService memberService;

    private final EmailService emailService;

    public List<ActivityGroupResponseDto> getActivityGroups(ActivityGroupCategory category) {
        List<ActivityGroup> activityGroupList = getActivityGroupByCategory(category);
        return activityGroupList.stream()
                .map(ActivityGroupResponseDto::of)
                .collect(Collectors.toList());
    }

    public ActivityGroupDetailResponseDto getActivityGroup(Long activityGroupId) {
    ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        return ActivityGroupDetailResponseDto.of(activityGroup);
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

    @Transactional
    public void applyActivityGroup(Long activityGroupId) throws MessagingException {
        Member member = memberService.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        if (!activityGroup.getStatus().equals(ActivityGroupStatus.ACTIVE)){
            throw new IllegalStateException("해당 활동은 진행중인 활동이 아닙니다.");
        }
        GroupMember groupMember = GroupMember.of(member, activityGroup);
        groupMember.setStatus(GroupMemberStatus.IN_PROGRESS);
        groupMemberRepository.save(groupMember);

        GroupMember groupLeader = groupMemberRepository.findByActivityGroupIdAndRole(activityGroupId, ActivityGroupRole.LEADER)
                .orElseThrow(() -> new NotFoundException("해당 활동의 리더가 존재하지 않습니다."));
        emailService.sendEmail(groupLeader.getMember().getEmail(), "활동 참가 신청이 들어왔습니다.", member.getName() + "에게서 활동 참가 신청이 들어왔습니다.", null);

    }

    private ActivityGroup getActivityGroupByIdOrThrow(Long activityGroupId) {
        return activityGroupRepository.findById(activityGroupId)
                .orElseThrow(() -> new NotFoundException("해당 활동이 존재하지 않습니다."));
    }

    private List<ActivityGroup> getActivityGroupByCategory(ActivityGroupCategory category) {
        return activityGroupRepository.findAllByCategory(category);
    }

    private List<GroupSchedule> getGroupScheduleByActivityGroupId(Long activityGroupId) {
        return groupScheduleRepository.findAllByActivityGroupId(activityGroupId);
    }

    private List<GroupMember> getGroupMemberByActivityGroupId(Long activityGroupId) {
        return groupMemberRepository.findAllByActivityGroupId(activityGroupId);
    }

}
