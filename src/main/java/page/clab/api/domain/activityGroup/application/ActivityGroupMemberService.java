package page.clab.api.domain.activityGroup.application;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.activityGroup.dao.ActivityGroupBoardRepository;
import page.clab.api.domain.activityGroup.dao.ActivityGroupDetailsRepository;
import page.clab.api.domain.activityGroup.dao.ActivityGroupRepository;
import page.clab.api.domain.activityGroup.dao.ApplyFormRepository;
import page.clab.api.domain.activityGroup.dao.GroupMemberRepository;
import page.clab.api.domain.activityGroup.dao.GroupScheduleRepository;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoardCategory;
import page.clab.api.domain.activityGroup.domain.ActivityGroupCategory;
import page.clab.api.domain.activityGroup.domain.ActivityGroupRole;
import page.clab.api.domain.activityGroup.domain.ActivityGroupStatus;
import page.clab.api.domain.activityGroup.domain.ApplyForm;
import page.clab.api.domain.activityGroup.domain.GroupMember;
import page.clab.api.domain.activityGroup.domain.GroupMemberStatus;
import page.clab.api.domain.activityGroup.domain.GroupSchedule;
import page.clab.api.domain.activityGroup.dto.param.ActivityGroupDetails;
import page.clab.api.domain.activityGroup.dto.param.GroupScheduleDto;
import page.clab.api.domain.activityGroup.dto.request.ApplyFormRequestDto;
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
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityGroupMemberService {

    private final MemberService memberService;

    private final NotificationService notificationService;

    private final ActivityGroupRepository activityGroupRepository;

    private final GroupScheduleRepository groupScheduleRepository;

    private final GroupMemberRepository groupMemberRepository;

    private final ActivityGroupBoardRepository activityGroupBoardRepository;

    private final ApplyFormRepository applyFormRepository;

    private final ActivityGroupDetailsRepository activityGroupDetailsRepository;

    public PagedResponseDto<ActivityGroupResponseDto> getActivityGroups(Pageable pageable) {
        Page<ActivityGroup> activityGroupList = activityGroupRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(activityGroupList.map(ActivityGroupResponseDto::of));
    }

    public Object getActivityGroup(Long activityGroupId) {
        ActivityGroupDetails details = activityGroupDetailsRepository.fetchActivityGroupDetails(activityGroupId);
        Member currentMember = memberService.getCurrentMember();

        boolean isOwner = details.getGroupMembers().stream()
                .anyMatch(groupMember -> groupMember.isOwnerAndLeader(currentMember));

        if (details.getActivityGroup().isStudy()) {
            return ActivityGroupStudyResponseDto.create(details.getActivityGroup(), details.getGroupMembers(), details.getActivityGroupBoards(), isOwner);
        } else if (details.getActivityGroup().isProject()) {
            return ActivityGroupProjectResponseDto.create(details.getActivityGroup(), details.getGroupMembers(), details.getActivityGroupBoards(), isOwner);
        } else {
            throw new InvalidCategoryException("해당 카테고리가 존재하지 않습니다.");
        }
    }

    public PagedResponseDto<ActivityGroupStatusResponseDto> getActivityGroupsByStatus(ActivityGroupStatus activityGroupStatus, Pageable pageable) {
        List<ActivityGroup> activityGroupList = getActivityGroupByStatus(activityGroupStatus);
        List<ActivityGroupStatusResponseDto> activityGroupStatusResponseDtos = activityGroupList.stream()
                .map(activityGroup -> {
                    Long participantCount = getGroupMemberByActivityGroupId(activityGroup.getId()).stream()
                            .filter(GroupMember::isAccepted)
                            .count();
                    GroupMember groupLeader = getGroupMemberByActivityGroupIdAndRole(activityGroup.getId(), ActivityGroupRole.LEADER);
                    Member leader = groupLeader != null ? groupLeader.getMember() : null;
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
        if (!activityGroup.isProgressing()) {
            throw new ActivityGroupNotProgressingException("해당 활동은 진행중인 활동이 아닙니다.");
        }
        if (isGroupMember(activityGroup, member)) {
            throw new AlreadyAppliedException("해당 활동에 신청한 내역이 존재합니다.");
        }

        ApplyForm form = ApplyForm.create(formRequestDto, activityGroup, member);
        applyFormRepository.save(form);

        GroupMember groupMember = GroupMember.create(member, activityGroup, ActivityGroupRole.MEMBER, GroupMemberStatus.WAITING);
        groupMemberRepository.save(groupMember);

        GroupMember groupLeader = getGroupMemberByActivityGroupIdAndRole(activityGroup.getId(), ActivityGroupRole.LEADER);
        if (groupLeader != null) {
            notificationService.sendNotificationToMember(
                    groupLeader.getMember(),
                    "[" + activityGroup.getName() + "] " + member.getName() + "님이 활동 참가 신청을 하였습니다."
            );
        }
        return activityGroup.getId();
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

    public GroupMember getGroupMemberByActivityGroupIdAndRole(Long activityGroupId, ActivityGroupRole role) {
        return groupMemberRepository.findByActivityGroupIdAndRole(activityGroupId, role)
                .orElse(null);
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
