package page.clab.api.domain.activityGroup.application;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.activityGroup.dao.ActivityGroupBoardRepository;
import page.clab.api.domain.activityGroup.dao.ActivityGroupRepository;
import page.clab.api.domain.activityGroup.dao.ApplyFormRepository;
import page.clab.api.domain.activityGroup.dao.GroupMemberRepository;
import page.clab.api.domain.activityGroup.dao.GroupScheduleRepository;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoardCategory;
import page.clab.api.domain.activityGroup.domain.ActivityGroupCategory;
import page.clab.api.domain.activityGroup.domain.ActivityGroupRole;
import page.clab.api.domain.activityGroup.domain.ActivityGroupStatus;
import page.clab.api.domain.activityGroup.domain.ApplyForm;
import page.clab.api.domain.activityGroup.domain.GroupMember;
import page.clab.api.domain.activityGroup.domain.GroupMemberStatus;
import page.clab.api.domain.activityGroup.domain.GroupSchedule;
import page.clab.api.domain.activityGroup.dto.param.GroupScheduleDto;
import page.clab.api.domain.activityGroup.dto.request.ApplyFormRequestDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupBoardResponseDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupMemberApplierResponseDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupProjectResponseDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupResponseDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupStatusResponseDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupStudyResponseDto;
import page.clab.api.domain.activityGroup.dto.response.GroupMemberResponseDto;
import page.clab.api.domain.activityGroup.exception.ActivityGroupNotProgressingException;
import page.clab.api.domain.activityGroup.exception.AlreadyAppliedException;
import page.clab.api.domain.activityGroup.exception.InvalidCategoryException;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.domain.notification.dto.request.NotificationRequestDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.email.application.EmailService;
import page.clab.api.global.common.email.domain.EmailTemplateType;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;
import java.util.Objects;

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

    private final ActivityGroupBoardRepository activityGroupBoardRepository;

    private final ApplyFormRepository applyFormRepository;

    public PagedResponseDto<ActivityGroupResponseDto> getActivityGroups(Pageable pageable) {
        Page<ActivityGroup> activityGroupList = activityGroupRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(activityGroupList.map(ActivityGroupResponseDto::of));
    }

    public Object getActivityGroup(Long activityGroupId) {
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        Member member = memberService.getCurrentMember();
        List<GroupMember> groupMembers = getGroupMemberByActivityGroupId(activityGroupId);
        GroupMember leader = groupMembers.stream()
                .filter(groupMember -> groupMember.getRole().equals(ActivityGroupRole.LEADER))
                .findFirst()
                .orElse(null);
        boolean isOwner = Objects.nonNull(leader) && leader.getMember().getId().equals(member.getId());

        List<ActivityGroupBoard> activityGroupBoards = activityGroupBoardRepository.findAllByActivityGroupIdOrderByCreatedAtDesc(activityGroupId);
        List<ActivityGroupBoardResponseDto> noticeAndWeeklyActivityBoards = activityGroupBoards.stream()
                .filter(activityGroupBoard -> activityGroupBoard.getCategory().equals(ActivityGroupBoardCategory.NOTICE) || activityGroupBoard.getCategory().equals(ActivityGroupBoardCategory.WEEKLY_ACTIVITY))
                .map(ActivityGroupBoardResponseDto::of)
                .toList();

        if (activityGroup.getCategory().equals(ActivityGroupCategory.STUDY)) {
            return ActivityGroupStudyResponseDto.of(activityGroup, GroupMemberResponseDto.of(groupMembers), noticeAndWeeklyActivityBoards, isOwner);
        } else if (activityGroup.getCategory().equals(ActivityGroupCategory.PROJECT)) {
            return ActivityGroupProjectResponseDto.of(activityGroup, GroupMemberResponseDto.of(groupMembers), noticeAndWeeklyActivityBoards, isOwner);
        } else {
            throw new InvalidCategoryException("해당 카테고리가 존재하지 않습니다.");
        }
    }

    public PagedResponseDto<ActivityGroupStatusResponseDto> getActivityGroupsByStatus(ActivityGroupStatus activityGroupStatus, Pageable pageable) {
        List<ActivityGroup> activityGroupList = getActivityGroupByStatus(activityGroupStatus);
        List<ActivityGroupStatusResponseDto> activityGroupStatusResponseDtos = activityGroupList.stream()
                .map(activityGroup -> {
                    Long participantCount = getGroupMemberByActivityGroupId(activityGroup.getId()).stream()
                            .filter(groupMember -> groupMember.getStatus().equals(GroupMemberStatus.ACCEPTED))
                            .count();
                    Member leader = getGroupMemberByActivityGroupIdAndRole(activityGroup.getId(), ActivityGroupRole.LEADER).getMember();
                    Long weeklyActivityCount = activityGroupBoardRepository.countByActivityGroupIdAndCategory(activityGroup.getId(), ActivityGroupBoardCategory.WEEKLY_ACTIVITY);
                    return ActivityGroupStatusResponseDto.of(activityGroup, leader, participantCount, weeklyActivityCount);
                })
                .toList();
        return new PagedResponseDto<>(activityGroupStatusResponseDtos, pageable, activityGroupStatusResponseDtos.size());
    }

    public PagedResponseDto<ActivityGroupResponseDto> getActivityGroupsByCategory(ActivityGroupCategory category, Pageable pageable) {
        Page<ActivityGroup> activityGroupList = getActivityGroupByCategory(category, pageable);
        return new PagedResponseDto<>(activityGroupList.map(ActivityGroupResponseDto::of));
    }

    public PagedResponseDto<GroupScheduleDto> getGroupSchedules(Long activityGroupId, Pageable pageable) {
        Page<GroupSchedule> groupSchedules = getGroupScheduleByActivityGroupId(activityGroupId, pageable);
        return new PagedResponseDto<>(groupSchedules.map(GroupScheduleDto::of));
    }

    public PagedResponseDto<GroupMemberResponseDto> getActivityGroupMembers(Long activityGroupId, Pageable pageable) {
        Page<GroupMember> groupMembers = getGroupMemberByActivityGroupIdAndStatus(activityGroupId, GroupMemberStatus.ACCEPTED, pageable);
        return new PagedResponseDto<>(groupMembers.map(GroupMemberResponseDto::of));
    }

    @Transactional
    public Long applyActivityGroup(Long activityGroupId, ApplyFormRequestDto formRequestDto) throws MessagingException {
        Member member = memberService.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        if (!activityGroup.getStatus().equals(ActivityGroupStatus.PROGRESSING)) {
            throw new ActivityGroupNotProgressingException("해당 활동은 진행중인 활동이 아닙니다.");
        }
        if (isGroupMember(activityGroup, member)) {
            throw new AlreadyAppliedException("해당 활동에 신청한 내역이 존재합니다.");
        }

        ApplyForm form = ApplyForm.of(formRequestDto);
        form.setActivityGroup(activityGroup);
        form.setMember(member);
        applyFormRepository.save(form);

        GroupMember groupMember = GroupMember.of(member, activityGroup);
        groupMember.setRole(ActivityGroupRole.MEMBER);
        groupMember.setStatus(GroupMemberStatus.WAITING);
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

    public ActivityGroupMemberApplierResponseDto getApplierInformation() {
        Member member = memberService.getCurrentMember();
        return ActivityGroupMemberApplierResponseDto.of(member);
    }

    public ActivityGroup getActivityGroupByIdOrThrow(Long activityGroupId) {
        return activityGroupRepository.findById(activityGroupId)
                .orElseThrow(() -> new NotFoundException("해당 활동이 존재하지 않습니다."));
    }

    public GroupMember getGroupMemberByActivityGroupAndMemberOrThrow(ActivityGroup activityGroup, Member member) {
        return groupMemberRepository.findByActivityGroupAndMember(activityGroup, member)
                .orElseThrow(() -> new NotFoundException("해당 멤버가 활동에 참여하지 않았습니다."));
    }

    public List<ActivityGroup> getActivityGroupByStatus(ActivityGroupStatus status) {
        return activityGroupRepository.findAllByStatusOrderByCreatedAtDesc(status);
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

    public boolean isGroupMember(ActivityGroup activityGroup, Member member) {
        return groupMemberRepository.existsByActivityGroupAndMember(activityGroup, member);
    }

    public GroupMember save(GroupMember groupMember) {
        return groupMemberRepository.save(groupMember);
    }

    public void deleteAll(List<GroupMember> groupMemberList) {
        groupMemberRepository.deleteAll(groupMemberList);
    }

}
