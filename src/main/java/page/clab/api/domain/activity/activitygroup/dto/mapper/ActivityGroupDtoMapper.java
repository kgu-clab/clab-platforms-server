package page.clab.api.domain.activity.activitygroup.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import page.clab.api.domain.activity.activitygroup.domain.Absent;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupReport;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupStatus;
import page.clab.api.domain.activity.activitygroup.domain.ApplyForm;
import page.clab.api.domain.activity.activitygroup.domain.Attendance;
import page.clab.api.domain.activity.activitygroup.domain.GroupMember;
import page.clab.api.domain.activity.activitygroup.domain.GroupSchedule;
import page.clab.api.domain.activity.activitygroup.dto.param.ActivityGroupDetails;
import page.clab.api.domain.activity.activitygroup.dto.param.GroupScheduleDto;
import page.clab.api.domain.activity.activitygroup.dto.request.AbsentRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.request.ActivityGroupBoardRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.request.ActivityGroupReportRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.request.ActivityGroupRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.request.ApplyFormRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.response.AbsentResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupBoardChildResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupBoardReferenceDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupBoardResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupBoardStatusUpdatedResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupDetailResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupMemberWithApplyReasonResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupReportResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupStatusResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.AssignmentSubmissionWithFeedbackResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.AttendanceResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.FeedbackResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.GroupMemberResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.LeaderInfo;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.common.file.dto.mapper.FileDtoMapper;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ActivityGroupDtoMapper {

    private final FileDtoMapper dtoMapper;

    public GroupSchedule fromDto(GroupScheduleDto groupScheduleDto, ActivityGroup activityGroup) {
        return GroupSchedule.builder()
                .activityGroup(activityGroup)
                .schedule(groupScheduleDto.getSchedule())
                .content(groupScheduleDto.getContent())
                .build();
    }

    public Absent fromDto(AbsentRequestDto requestDto, Member absentee, ActivityGroup activityGroup) {
        return Absent.builder()
                .memberId(absentee.getId())
                .activityGroup(activityGroup)
                .absentDate(requestDto.getAbsentDate())
                .reason(requestDto.getReason())
                .build();
    }

    public ActivityGroupBoard fromDto(ActivityGroupBoardRequestDto requestDto, Member member, ActivityGroup activityGroup, ActivityGroupBoard parentBoard, List<UploadedFile> uploadedFiles) {
        return ActivityGroupBoard.builder()
                .activityGroup(activityGroup)
                .memberId(member.getId())
                .category(requestDto.getCategory())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .parent(parentBoard)
                .uploadedFiles(uploadedFiles)
                .dueDateTime(requestDto.getDueDateTime())
                .isDeleted(false)
                .build();
    }

    public ActivityGroupReport fromDto(ActivityGroupReportRequestDto requestDto, ActivityGroup activityGroup) {
        return ActivityGroupReport.builder()
                .turn(requestDto.getTurn())
                .activityGroup(activityGroup)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .isDeleted(false)
                .build();
    }

    public ActivityGroup fromDto(ActivityGroupRequestDto requestDto) {
        return ActivityGroup.builder()
                .category(requestDto.getCategory())
                .subject(requestDto.getSubject())
                .name(requestDto.getName())
                .content(requestDto.getContent())
                .status(ActivityGroupStatus.WAITING)
                .progress(0L)
                .imageUrl(requestDto.getImageUrl())
                .curriculum(requestDto.getCurriculum())
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .techStack(requestDto.getTechStack())
                .githubUrl(requestDto.getGithubUrl())
                .isDeleted(false)
                .build();
    }

    public ApplyForm fromDto(ApplyFormRequestDto requestDto, ActivityGroup activityGroup, Member member) {
        return ApplyForm.builder()
                .activityGroup(activityGroup)
                .memberId(member.getId())
                .applyReason(requestDto.getApplyReason())
                .build();
    }

    public GroupScheduleDto toDto(GroupSchedule groupSchedule) {
        return GroupScheduleDto.builder()
                .schedule(groupSchedule.getSchedule())
                .content(groupSchedule.getContent())
                .build();
    }

    public AbsentResponseDto toDto(Absent absent, Member member) {
        return AbsentResponseDto.builder()
                .absenteeId(member.getId())
                .absenteeName(member.getName())
                .activityGroupId(absent.getActivityGroup().getId())
                .activityGroupName(absent.getActivityGroup().getName())
                .reason(absent.getReason())
                .absentDate(absent.getAbsentDate())
                .build();
    }


    public ActivityGroupDetailResponseDto toDto(ActivityGroup activityGroup, List<ActivityGroupBoardResponseDto> boards, List<GroupMemberResponseDto> groupMemberResponseDtos, boolean isOwner) {
        return ActivityGroupDetailResponseDto.builder()
                .id(activityGroup.getId())
                .category(activityGroup.getCategory())
                .subject(activityGroup.getSubject())
                .name(activityGroup.getName())
                .content(activityGroup.getContent())
                .status(activityGroup.getStatus())
                .progress(activityGroup.getProgress())
                .imageUrl(activityGroup.getImageUrl())
                .curriculum(activityGroup.getCurriculum())
                .groupMembers(groupMemberResponseDtos)
                .startDate(activityGroup.getStartDate())
                .endDate(activityGroup.getEndDate())
                .techStack(activityGroup.getTechStack())
                .githubUrl(activityGroup.getGithubUrl())
                .activityGroupBoards(boards)
                .isOwner(isOwner)
                .createdAt(activityGroup.getCreatedAt())
                .build();
    }

    public ActivityGroupMemberWithApplyReasonResponseDto toDto(Member member, GroupMember groupMember, String applyReason) {
        return ActivityGroupMemberWithApplyReasonResponseDto.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .role(groupMember.getRole().toString())
                .status(groupMember.getStatus())
                .applyReason(applyReason)
                .build();
    }

    public ActivityGroupReportResponseDto toDto(ActivityGroupReport report) {
        return ActivityGroupReportResponseDto.builder()
                .activityGroupId(report.getActivityGroup().getId())
                .activityGroupName(report.getActivityGroup().getName())
                .turn(report.getTurn())
                .title(report.getTitle())
                .content(report.getContent())
                .createdAt(report.getCreatedAt())
                .updatedAt(report.getUpdatedAt())
                .build();
    }

    public ActivityGroupResponseDto toDto(ActivityGroup activityGroup) {
        return ActivityGroupResponseDto.builder()
                .id(activityGroup.getId())
                .name(activityGroup.getName())
                .category(activityGroup.getCategory())
                .subject(activityGroup.getSubject())
                .status(activityGroup.getStatus())
                .imageUrl(activityGroup.getImageUrl())
                .createdAt(activityGroup.getCreatedAt())
                .build();
    }

    public ActivityGroupStatusResponseDto toDto(ActivityGroup activityGroup, List<LeaderInfo> leader, Long participantCount, Long weeklyActivityCount) {
        return ActivityGroupStatusResponseDto.builder()
                .id(activityGroup.getId())
                .name(activityGroup.getName())
                .content(activityGroup.getContent())
                .category(activityGroup.getCategory())
                .subject(activityGroup.getSubject())
                .imageUrl(activityGroup.getImageUrl())
                .leaders(CollectionUtils.isEmpty(leader) ? null : leader)
                .participantCount(participantCount)
                .weeklyActivityCount(weeklyActivityCount)
                .createdAt(activityGroup.getCreatedAt())
                .build();
    }

    public AttendanceResponseDto toDto(Attendance attendance) {
        return AttendanceResponseDto.builder()
                .activityGroupId(attendance.getActivityGroup().getId())
                .memberId(attendance.getMemberId())
                .attendanceDateTime(attendance.getCreatedAt())
                .build();
    }

    public GroupMemberResponseDto toDto(Member member, GroupMember groupMember) {
        return GroupMemberResponseDto.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .role(groupMember.getRole().getKey())
                .status(groupMember.getStatus())
                .build();
    }

    public ActivityGroupBoardChildResponseDto toActivityGroupBoardChildResponseDto(ActivityGroupBoard board, MemberBasicInfoDto memberBasicInfoDto, List<ActivityGroupBoardChildResponseDto> childrenDtos) {
        return ActivityGroupBoardChildResponseDto.builder()
                .id(board.getId())
                .memberId(memberBasicInfoDto.getMemberId())
                .memberName(memberBasicInfoDto.getMemberName())
                .category(board.getCategory())
                .title(board.getTitle())
                .content(board.getContent())
                .dueDateTime(board.getDueDateTime())
                .updatedAt(board.getUpdatedAt())
                .createdAt(board.getCreatedAt())
                .files(dtoMapper.toDto(board.getUploadedFiles()))
                .children(childrenDtos)
                .build();
    }

    public AssignmentSubmissionWithFeedbackResponseDto toAssignmentSubmissionWithFeedbackResponseDto(ActivityGroupBoard board, MemberBasicInfoDto memberBasicInfo, List<FeedbackResponseDto> feedbackDtos) {
        return AssignmentSubmissionWithFeedbackResponseDto.builder()
                .id(board.getId())
                .memberId(memberBasicInfo.getMemberId())
                .memberName(memberBasicInfo.getMemberName())
                .parentId(board.getParent() != null ? board.getParent().getId() : null)
                .content(board.getContent())
                .files(dtoMapper.toDto(board.getUploadedFiles()))
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .feedbacks(feedbackDtos)
                .build();
    }

    public ActivityGroupBoardResponseDto toActivityGroupBoardResponseDto(ActivityGroupBoard board, MemberBasicInfoDto memberBasicInfoDto) {
        return ActivityGroupBoardResponseDto.builder()
                .id(board.getId())
                .memberId(memberBasicInfoDto.getMemberId())
                .memberName(memberBasicInfoDto.getMemberName())
                .parentId(board.getParent() != null ? board.getParent().getId() : null)
                .category(board.getCategory())
                .title(board.getTitle())
                .content(board.getContent())
                .files(dtoMapper.toDto(board.getUploadedFiles()))
                .dueDateTime(board.getDueDateTime())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

    public FeedbackResponseDto toFeedbackResponseDto(ActivityGroupBoard board, MemberBasicInfoDto memberBasicInfo) {
        return FeedbackResponseDto.builder()
                .id(board.getId())
                .memberId(memberBasicInfo.getMemberId())
                .memberName(memberBasicInfo.getMemberName())
                .content(board.getContent())
                .files(dtoMapper.toDto(board.getUploadedFiles()))
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

    public ActivityGroupDetails of(ActivityGroup activityGroup, List<GroupMember> groupMembers, List<ActivityGroupBoard> activityGroupBoards) {
        return ActivityGroupDetails.builder()
                .activityGroup(activityGroup)
                .groupMembers(groupMembers)
                .activityGroupBoards(activityGroupBoards)
                .build();
    }

    public ActivityGroupBoardStatusUpdatedResponseDto of(Long groupId, ActivityGroupStatus activityGroupStatus) {
        return ActivityGroupBoardStatusUpdatedResponseDto.builder()
                .id(groupId)
                .activityGroupStatus(activityGroupStatus)
                .build();
    }

    public ActivityGroupBoardReferenceDto of(Long id, Long groupId, Long parentId) {
        return ActivityGroupBoardReferenceDto.builder()
                .id(id)
                .groupId(groupId)
                .parentId(parentId)
                .build();
    }

    public LeaderInfo create(Member leader, LocalDateTime createdAt) {
        return LeaderInfo.builder()
                .id(leader.getId())
                .name(leader.getName())
                .createdAt(createdAt)
                .build();
    }
}
