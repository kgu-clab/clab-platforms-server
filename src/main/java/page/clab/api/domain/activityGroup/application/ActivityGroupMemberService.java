package page.clab.api.domain.activityGroup.application;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.activityGroup.dao.ActivityGroupRepository;
import page.clab.api.domain.activityGroup.dao.GroupMemberRepository;
import page.clab.api.domain.activityGroup.dao.GroupScheduleRepository;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupCategory;
import page.clab.api.domain.activityGroup.domain.ActivityGroupRole;
import page.clab.api.domain.activityGroup.domain.ActivityGroupStatus;
import page.clab.api.domain.activityGroup.domain.GroupMember;
import page.clab.api.domain.activityGroup.domain.GroupMemberStatus;
import page.clab.api.domain.activityGroup.domain.GroupSchedule;
import page.clab.api.domain.activityGroup.dto.param.GroupScheduleDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupProjectResponseDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupResponseDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupStudyResponseDto;
import page.clab.api.domain.activityGroup.dto.response.GroupMemberResponseDto;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.domain.notification.dto.request.NotificationRequestDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.email.application.EmailService;
import page.clab.api.global.common.email.domain.EmailTemplateType;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityGroupMemberService {

    private final MemberService memberService;

    private final EmailService emailService;

    private final NotificationService notificationService;

    private final ActivityGroupRepository activityGroupRepository;

    private final GroupScheduleRepository groupScheduleRepository;

    private final GroupMemberRepository groupMemberRepository;

    public PagedResponseDto<ActivityGroupResponseDto> getActivityGroups(Pageable pageable) {
        Page<ActivityGroup> activityGroupList = activityGroupRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(activityGroupList.map(ActivityGroupResponseDto::of));
    }

    public PagedResponseDto<ActivityGroupResponseDto> getActivityGroupsByCategory(ActivityGroupCategory category, Pageable pageable) {
        Page<ActivityGroup> activityGroupList = getActivityGroupByCategory(category, pageable);
        return new PagedResponseDto<>(activityGroupList.map(ActivityGroupResponseDto::of));
    }

    public ActivityGroupStudyResponseDto getActivityGroupStudy(Long activityGroupId) {
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        if (!activityGroup.getCategory().equals(ActivityGroupCategory.STUDY)) {
            throw new IllegalStateException("해당 활동은 스터디 활동이 아닙니다.");
        }
        List<GroupMember> groupMembers = getGroupMemberByActivityGroupId(activityGroupId);
        ActivityGroupStudyResponseDto activityGroupStudyResponseDto = ActivityGroupStudyResponseDto.of(activityGroup);
        activityGroupStudyResponseDto.setGroupMembers(GroupMemberResponseDto.of(groupMembers));
        return activityGroupStudyResponseDto;
    }

    public ActivityGroupProjectResponseDto getActivityGroupProject(Long activityGroupId) {
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        if (!activityGroup.getCategory().equals(ActivityGroupCategory.PROJECT)) {
            throw new IllegalStateException("해당 활동은 프로젝트 활동이 아닙니다.");
        }
        List<GroupMember> groupMembers = getGroupMemberByActivityGroupId(activityGroupId);
        ActivityGroupProjectResponseDto activityGroupProjectResponseDto = ActivityGroupProjectResponseDto.of(activityGroup);
        activityGroupProjectResponseDto.setGroupMembers(GroupMemberResponseDto.of(groupMembers));
        return activityGroupProjectResponseDto;
    }

    public PagedResponseDto<GroupScheduleDto> getGroupSchedules(Long activityGroupId, Pageable pageable) {
        Page<GroupSchedule> groupSchedules = getGroupScheduleByActivityGroupId(activityGroupId, pageable);
        return new PagedResponseDto<>(groupSchedules.map(GroupScheduleDto::of));
    }

    public PagedResponseDto<GroupMemberResponseDto> getActivityGroupMembers(Long activityGroupId, Pageable pageable) {
        Page<GroupMember> groupMembers = getGroupMemberByActivityGroupId(activityGroupId, pageable);
        return new PagedResponseDto<>(groupMembers.map(GroupMemberResponseDto::of));
    }

    @Transactional
    public Long applyActivityGroup(Long activityGroupId) throws MessagingException {
        Member member = memberService.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        if (!activityGroup.getStatus().equals(ActivityGroupStatus.PROGRESSING)) {
            throw new IllegalStateException("해당 활동은 진행중인 활동이 아닙니다.");
        }
        GroupMember groupMember = GroupMember.of(member, activityGroup);
        groupMember.setRole(ActivityGroupRole.MEMBER);
        groupMember.setStatus(GroupMemberStatus.IN_PROGRESS);
        groupMemberRepository.save(groupMember);
        GroupMember groupLeader = getGroupMemberByActivityGroupIdAndRole(activityGroup.getId(), ActivityGroupRole.LEADER);
        String subject = "[" + activityGroup.getName() + "] 활동 참가 신청이 들어왔습니다.";
        String content = member.getName() + "에게서 활동 참가 신청이 들어왔습니다.";
        emailService.sendEmailAsync(groupLeader.getMember().getEmail(), subject, content, null, EmailTemplateType.NORMAL);
        NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                .memberId(groupLeader.getMember().getId())
                .content("[" + activityGroup.getName() + "] " + member.getName() + "님이 활동 참가 신청을 하였습니다.")
                .build();
        notificationService.createNotification(notificationRequestDto);
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

    public GroupMember getGroupMemberByActivityGroupIdAndRole(Long activityGroupId, ActivityGroupRole role) {
        return groupMemberRepository.findByActivityGroupIdAndRole(activityGroupId, role)
                .orElseThrow(() -> new NotFoundException("해당 활동의 리더가 존재하지 않습니다."));
    }

    public List<GroupMember> getGroupMemberByMember(Member member){
        return groupMemberRepository.findAllByMember(member);
    }

    public GroupMember save(GroupMember groupMember) {
        return groupMemberRepository.save(groupMember);
    }

    public void deleteAll(List<GroupMember> groupMemberList) {
        groupMemberRepository.deleteAll(groupMemberList);
    }

}
