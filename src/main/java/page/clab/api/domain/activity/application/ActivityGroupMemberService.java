package page.clab.api.domain.activity.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activity.dao.ActivityGroupBoardRepository;
import page.clab.api.domain.activity.dao.ActivityGroupDetailsRepository;
import page.clab.api.domain.activity.dao.ActivityGroupRepository;
import page.clab.api.domain.activity.dao.ApplyFormRepository;
import page.clab.api.domain.activity.dao.GroupMemberRepository;
import page.clab.api.domain.activity.dao.GroupScheduleRepository;
import page.clab.api.domain.activity.domain.ActivityGroup;
import page.clab.api.domain.activity.domain.ActivityGroupBoardCategory;
import page.clab.api.domain.activity.domain.ActivityGroupCategory;
import page.clab.api.domain.activity.domain.ActivityGroupRole;
import page.clab.api.domain.activity.domain.ActivityGroupStatus;
import page.clab.api.domain.activity.domain.ApplyForm;
import page.clab.api.domain.activity.domain.GroupMember;
import page.clab.api.domain.activity.domain.GroupMemberStatus;
import page.clab.api.domain.activity.domain.GroupSchedule;
import page.clab.api.domain.activity.dto.param.ActivityGroupDetails;
import page.clab.api.domain.activity.dto.param.GroupScheduleDto;
import page.clab.api.domain.activity.dto.request.ApplyFormRequestDto;
import page.clab.api.domain.activity.dto.response.ActivityGroupProjectResponseDto;
import page.clab.api.domain.activity.dto.response.ActivityGroupResponseDto;
import page.clab.api.domain.activity.dto.response.ActivityGroupStatusResponseDto;
import page.clab.api.domain.activity.dto.response.ActivityGroupStudyResponseDto;
import page.clab.api.domain.activity.dto.response.GroupMemberResponseDto;
import page.clab.api.domain.activity.exception.AlreadyAppliedException;
import page.clab.api.domain.activity.exception.InvalidCategoryException;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.application.port.in.SendNotificationUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityGroupMemberService {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final SendNotificationUseCase notificationService;
    private final ActivityGroupRepository activityGroupRepository;
    private final GroupScheduleRepository groupScheduleRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ActivityGroupBoardRepository activityGroupBoardRepository;
    private final ApplyFormRepository applyFormRepository;
    private final ActivityGroupDetailsRepository activityGroupDetailsRepository;

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupResponseDto> getActivityGroups(Pageable pageable) {
        Page<ActivityGroup> activityGroups = activityGroupRepository.findAll(pageable);
        return new PagedResponseDto<>(activityGroups.map(ActivityGroupResponseDto::toDto));
    }

    @Transactional(readOnly = true)
    public Object getActivityGroup(Long activityGroupId) {
        ActivityGroupDetails details = activityGroupDetailsRepository.fetchActivityGroupDetails(activityGroupId);
        Member currentMember = retrieveMemberUseCase.getCurrentMember();

        boolean isOwner = details.getGroupMembers().stream()
                .anyMatch(groupMember -> groupMember.isOwnerAndLeader(currentMember));

        List<GroupMemberResponseDto> groupMemberResponseDtos = details.getGroupMembers().stream()
                .map(groupMember -> GroupMemberResponseDto.toDto(retrieveMemberUseCase.findByIdOrThrow(groupMember.getMemberId()), groupMember))
                .toList();

        if (details.getActivityGroup().isStudy()) {
            return ActivityGroupStudyResponseDto.create(details.getActivityGroup(), details.getGroupMembers(), details.getActivityGroupBoards(), groupMemberResponseDtos, isOwner);
        } else if (details.getActivityGroup().isProject()) {
            return ActivityGroupProjectResponseDto.create(details.getActivityGroup(), details.getGroupMembers(), details.getActivityGroupBoards(), groupMemberResponseDtos, isOwner);
        } else {
            throw new InvalidCategoryException("해당 카테고리가 존재하지 않습니다.");
        }
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupResponseDto> getMyActivityGroups(Pageable pageable) {
        String currentMemberId = retrieveMemberUseCase.getCurrentMemberId();
        List<GroupMember> groupMembers = getGroupMemberByMemberId(currentMemberId);

        List<ActivityGroupResponseDto> activityGroups = groupMembers.stream()
                .filter(GroupMember::isAccepted)
                .map(GroupMember::getActivityGroup)
                .map(ActivityGroupResponseDto::toDto)
                .toList();

        return new PagedResponseDto<>(activityGroups, pageable, activityGroups.size());
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupStatusResponseDto> getActivityGroupsByStatus(ActivityGroupStatus status, Pageable pageable) {
        List<ActivityGroup> activityGroups = activityGroupRepository.findActivityGroupsByStatus(status);

        List<ActivityGroupStatusResponseDto> activityGroupDtos = activityGroups.stream().map(activityGroup -> {
            Long participantCount = groupMemberRepository.countAcceptedMembersByActivityGroupId(activityGroup.getId());
            GroupMember leader = groupMemberRepository.findLeaderByActivityGroupId(activityGroup.getId());

            Member leaderMember = null;
            if (leader != null) {
                leaderMember = retrieveMemberUseCase.findByIdOrThrow(leader.getMemberId());
            }
            Long weeklyActivityCount = activityGroupBoardRepository.countByActivityGroupIdAndCategory(activityGroup.getId(), ActivityGroupBoardCategory.WEEKLY_ACTIVITY);

            return ActivityGroupStatusResponseDto.toDto(activityGroup, leaderMember, participantCount, weeklyActivityCount);
        }).toList();

        return new PagedResponseDto<>(activityGroupDtos, pageable, activityGroupDtos.size());
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupResponseDto> getActivityGroupsByCategory(ActivityGroupCategory category, Pageable pageable) {
        Page<ActivityGroup> activityGroupList = getActivityGroupByCategory(category, pageable);
        return new PagedResponseDto<>(activityGroupList.map(ActivityGroupResponseDto::toDto));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<GroupScheduleDto> getGroupSchedules(Long activityGroupId, Pageable pageable) {
        Page<GroupSchedule> groupSchedules = getGroupScheduleByActivityGroupId(activityGroupId, pageable);
        return new PagedResponseDto<>(groupSchedules.map(GroupScheduleDto::toDto));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<GroupMemberResponseDto> getActivityGroupMembers(Long activityGroupId, Pageable pageable) {
        Page<GroupMember> groupMembers = getGroupMemberByActivityGroupIdAndStatus(activityGroupId, GroupMemberStatus.ACCEPTED, pageable);
        return new PagedResponseDto<>(groupMembers.map(groupMember -> {
            Member member = retrieveMemberUseCase.findByIdOrThrow(groupMember.getMemberId());
            return GroupMemberResponseDto.toDto(member, groupMember);
        }));
    }

    @Transactional
    public Long applyActivityGroup(Long activityGroupId, ApplyFormRequestDto formRequestDto) {
        Member currentMember = retrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = getActivityGroupByIdOrThrow(activityGroupId);
        activityGroup.validateForApplication();
        if (isGroupMember(activityGroup, currentMember.getId())) {
            throw new AlreadyAppliedException("해당 활동에 신청한 내역이 존재합니다.");
        }

        ApplyForm form = ApplyFormRequestDto.toEntity(formRequestDto, activityGroup, currentMember);
        applyFormRepository.save(form);

        GroupMember groupMember = GroupMember.create(currentMember.getId(), activityGroup, ActivityGroupRole.NONE, GroupMemberStatus.WAITING);
        groupMemberRepository.save(groupMember);

        GroupMember groupLeader = getGroupMemberByActivityGroupIdAndRole(activityGroup.getId(), ActivityGroupRole.LEADER);
        if (groupLeader != null) {
            notificationService.sendNotificationToMember(groupLeader.getMemberId(), "[" + activityGroup.getName() + "] " + currentMember.getName() + "님이 활동 참가 신청을 하였습니다.");
        }
        return activityGroup.getId();
    }

    public ActivityGroup getActivityGroupByIdOrThrow(Long activityGroupId) {
        return activityGroupRepository.findById(activityGroupId)
                .orElseThrow(() -> new NotFoundException("해당 활동이 존재하지 않습니다."));
    }

    public GroupMember getGroupMemberByActivityGroupAndMemberOrThrow(ActivityGroup activityGroup, String memberId) {
        return groupMemberRepository.findByActivityGroupAndMemberId(activityGroup, memberId)
                .orElseThrow(() -> new NotFoundException("해당 멤버가 활동에 참여하지 않았습니다."));
    }

    private Page<ActivityGroup> getActivityGroupByCategory(ActivityGroupCategory category, Pageable pageable) {
        return activityGroupRepository.findAllByCategory(category, pageable);
    }

    private Page<GroupSchedule> getGroupScheduleByActivityGroupId(Long activityGroupId, Pageable pageable) {
        return groupScheduleRepository.findAllByActivityGroupId(activityGroupId, pageable);
    }

    public List<GroupMember> getGroupMemberByActivityGroupId(Long activityGroupId) {
        return groupMemberRepository.findAllByActivityGroupIdOrderByMemberIdAsc(activityGroupId);
    }

    public Page<GroupMember> getGroupMemberByActivityGroupId(Long activityGroupId, Pageable pageable) {
        return groupMemberRepository.findAllByActivityGroupId(activityGroupId, pageable);
    }

    public Page<GroupMember> getGroupMemberByActivityGroupIdAndStatus(Long activityGroupId, GroupMemberStatus status, Pageable pageable) {
        return groupMemberRepository.findAllByActivityGroupIdAndStatus(activityGroupId, status, pageable);
    }

    public GroupMember getGroupMemberByActivityGroupIdAndRole(Long activityGroupId, ActivityGroupRole role) {
        return groupMemberRepository.findByActivityGroupIdAndRole(activityGroupId, role)
                .orElse(null);
    }

    public List<GroupMember> getGroupMemberByMemberId(String memberId) {
        return groupMemberRepository.findAllByMemberId(memberId);
    }

    public boolean isGroupMember(ActivityGroup activityGroup, String memberId) {
        return groupMemberRepository.existsByActivityGroupAndMemberId(activityGroup, memberId);
    }

    public GroupMember save(GroupMember groupMember) {
        return groupMemberRepository.save(groupMember);
    }

    public void deleteAll(List<GroupMember> groupMemberList) {
        groupMemberRepository.deleteAll(groupMemberList);
    }

}
