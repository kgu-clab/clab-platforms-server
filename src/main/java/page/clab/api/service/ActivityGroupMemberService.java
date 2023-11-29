package page.clab.api.service;

import java.util.List;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.repository.ActivityGroupRepository;
import page.clab.api.repository.GroupMemberRepository;
import page.clab.api.repository.GroupScheduleRepository;
import page.clab.api.type.dto.ActivityGroupDetailsResponseDto;
import page.clab.api.type.dto.ActivityGroupResponseDto;
import page.clab.api.type.dto.GroupMemberDto;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityGroupMemberService {

    private final MemberService memberService;

    private final EmailService emailService;

    private final ActivityGroupRepository activityGroupRepository;

    private final GroupScheduleRepository groupScheduleRepository;

    private final GroupMemberRepository groupMemberRepository;

    public PagedResponseDto<ActivityGroupResponseDto> getActivityGroups(ActivityGroupCategory category, Pageable pageable) {
        Page<ActivityGroup> activityGroupList = getActivityGroupByCategory(category, pageable);
        return new PagedResponseDto<>(activityGroupList.map(ActivityGroupResponseDto::of));
    }

    public ActivityGroupDetailsResponseDto getActivityGroup(Long activityGroupId) {
    ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        return ActivityGroupDetailsResponseDto.of(activityGroup);
    }

    public PagedResponseDto<GroupScheduleDto> getGroupSchedules(Long activityGroupId, Pageable pageable) {
        Page<GroupSchedule> groupSchedules = getGroupScheduleByActivityGroupId(activityGroupId, pageable);
        return new PagedResponseDto<>(groupSchedules.map(GroupScheduleDto::of));
    }

    public PagedResponseDto<GroupMemberDto> getActivityGroupMembers(Long activityGroupId, Pageable pageable) {
        Page<GroupMember> groupMembers = getGroupMemberByActivityGroupId(activityGroupId, pageable);
        return new PagedResponseDto<>(groupMembers.map(GroupMemberDto::of));
    }

    @Transactional
    public Long applyActivityGroup(Long activityGroupId) throws MessagingException {
        Member member = memberService.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        if (!activityGroup.getStatus().equals(ActivityGroupStatus.ACTIVE)){
            throw new IllegalStateException("해당 활동은 진행중인 활동이 아닙니다.");
        }
        GroupMember groupMember = GroupMember.of(member, activityGroup);
        groupMember.setStatus(GroupMemberStatus.IN_PROGRESS);
        groupMemberRepository.save(groupMember);
        GroupMember groupLeader = getGroupMemberByActivityGroupIdAndRole(activityGroup.getId(), ActivityGroupRole.LEADER);
        String subject = "[" + activityGroup.getName() + "] 활동 참가 신청이 들어왔습니다.";
        String content = member.getName() + "에게서 활동 참가 신청이 들어왔습니다.";
        emailService.sendEmail(groupLeader.getMember().getEmail(), subject, content, null);
        return activityGroup.getId();
    }

    public ActivityGroup getActivityGroupByIdOrThrow(Long activityGroupId) {
        return activityGroupRepository.findById(activityGroupId)
                .orElseThrow(() -> new NotFoundException("해당 활동이 존재하지 않습니다."));
    }

    private Page<ActivityGroup> getActivityGroupByCategory(ActivityGroupCategory category, Pageable pageable) {
        return activityGroupRepository.findAllByCategoryOrderByCreatedAtDesc(category, pageable);
    }

    private Page<GroupSchedule> getGroupScheduleByActivityGroupId(Long activityGroupId, Pageable pageable) {
        return groupScheduleRepository.findAllByActivityGroupIdOrderByIdDesc(activityGroupId, pageable);
    }

    public List<GroupMember> getGroupMemberByActivityGroupId(Long activityGroupId) {
        return groupMemberRepository.findAllByActivityGroupIdOrderByMember_IdAsc(activityGroupId);
    }

    public Page<GroupMember> getGroupMemberByActivityGroupId(Long activityGroupId, Pageable pageable) {
        return groupMemberRepository.findAllByActivityGroupIdOrderByMember_IdAsc(activityGroupId, pageable);
    }

    public Page<GroupMember> getGroupMemberByActivityGroupIdAndStatus(Long activityGroupId, GroupMemberStatus status, Pageable pageable) {
        return groupMemberRepository.findAllByActivityGroupIdAndStatusOrderByMember_IdAsc(activityGroupId, status, pageable);
    }

    public GroupMember getGroupMemberByMemberOrThrow(Member member) {
        return groupMemberRepository.findByMember(member)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 멤버입니다."));
    }

    private GroupMember getGroupMemberByActivityGroupIdAndRole(Long activityGroupId, ActivityGroupRole role) {
        log.info("activityGroupId: {}, role: {}, result: {}", activityGroupId, role, groupMemberRepository.findByActivityGroupIdAndRole(activityGroupId, role).get());
        return groupMemberRepository.findByActivityGroupIdAndRole(activityGroupId, role)
                .orElseThrow(() -> new NotFoundException("해당 활동의 리더가 존재하지 않습니다."));
    }

    public GroupMember save(GroupMember groupMember) {
        return groupMemberRepository.save(groupMember);
    }

    public void deleteAll(List<GroupMember> groupMemberList) {
        groupMemberRepository.deleteAll(groupMemberList);
    }

}
