package page.clab.api.domain.activity.activitygroup.application;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activity.activitygroup.dao.ActivityGroupBoardRepository;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoardCategory;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupRole;
import page.clab.api.domain.activity.activitygroup.domain.GroupMember;
import page.clab.api.domain.activity.activitygroup.dto.request.ActivityGroupBoardRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.request.ActivityGroupBoardUpdateRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupBoardChildResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupBoardReferenceDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupBoardResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.AssignmentSubmissionWithFeedbackResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.FeedbackResponseDto;
import page.clab.api.domain.activity.activitygroup.exception.AlreadySubmittedThisWeekAssignmentException;
import page.clab.api.domain.activity.activitygroup.exception.InvalidParentBoardException;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.domain.memberManagement.member.domain.Role;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.external.memberManagement.notification.application.port.ExternalSendNotificationUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.file.application.UploadedFileService;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityGroupBoardService {

    private final ActivityGroupBoardRepository activityGroupBoardRepository;
    private final ActivityGroupAdminService activityGroupAdminService;
    private final ActivityGroupMemberService activityGroupMemberService;
    private final UploadedFileService uploadedFileService;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalSendNotificationUseCase externalSendNotificationUseCase;

    @Transactional
    public ActivityGroupBoardReferenceDto createActivityGroupBoard(Long parentId, Long activityGroupId, ActivityGroupBoardRequestDto requestDto) throws PermissionDeniedException {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);
        if (!activityGroupMemberService.isGroupMember(activityGroup, currentMember.getId())) {
            throw new PermissionDeniedException("활동 그룹 멤버만 게시글을 등록할 수 있습니다.");
        }

        validateCanCreateBoard(activityGroup, requestDto.getCategory(), currentMember);
        validateParentBoard(requestDto.getCategory(), parentId);
        validateAlreadySubmittedAssignmentThisWeek(requestDto.getCategory(), parentId, currentMember.getId());

        List<UploadedFile> uploadedFiles = uploadedFileService.getUploadedFilesByUrls(requestDto.getFileUrls());

        ActivityGroupBoard parentBoard = parentId != null ? getActivityGroupBoardByIdOrThrow(parentId) : null;
        ActivityGroupBoard board = ActivityGroupBoardRequestDto.toEntity(requestDto, currentMember, activityGroup, parentBoard, uploadedFiles);
        board.validateEssentialElementByCategory();

        if (parentId != null) {
            parentBoard.addChild(board);
            activityGroupBoardRepository.save(parentBoard);
        }
        activityGroupBoardRepository.save(board);

        notifyMembersAboutNewBoard(activityGroupId, activityGroup, currentMember);
        return ActivityGroupBoardReferenceDto.toDto(board.getId(), activityGroupId, parentId);
    }

    private void validateCanCreateBoard(ActivityGroup activityGroup, ActivityGroupBoardCategory category, Member currentMember) throws PermissionDeniedException {
        Role role = currentMember.getRole();
        boolean isRequireAdminOrLeaderCategory = category.isNotice() ||
                                                 category.isWeeklyActivity() ||
                                                 category.isAssignment() ||
                                                 category.isFeedback();

        // NOTICE, WEEKLY_ACTIVITY, ASSIGNMENT, FEEDBACK 카테고리에서 권한이 ADMIN 이상이 아니거나, 리더가 아니면 예외처리
        if (isRequireAdminOrLeaderCategory && !(role.isHigherThanOrEqual(Role.ADMIN) || activityGroupAdminService.isMemberGroupLeaderRole(activityGroup, currentMember))) {
            throw new PermissionDeniedException("해당 카테고리에서 게시글을 작성할 권한이 없습니다.");
        }
    }

    private boolean hasAccessToBoard(ActivityGroup activityGroup, ActivityGroupBoard board, Member currentMember) {
        // 카테고리가 SUBMIT이거나, FEEDBACK일 시, 제출자 또는 리더만이 해당 게시물에 접근 가능
        if (board.getCategory().isSubmit() || board.getCategory().isFeedback()) {
            return isSubmitterOrLeader(activityGroup, board, currentMember);
        }
        return true;
    }

    private boolean isSubmitterOrLeader(ActivityGroup activityGroup, ActivityGroupBoard board, Member currentMember) {
        boolean isSubmitter = board.getMemberId().equals(currentMember.getId());
        boolean isLeader = activityGroupAdminService.isMemberGroupLeaderRole(activityGroup, currentMember);
        // FEEDBACK을 가져오기 위해, parent의 카테고리가 SUBMIT이고, 현재 로그인한 멤버인지 확인
        if (board.getCategory().isFeedback()) {
            ActivityGroupBoard parentBoard = board.getParent();
            boolean isParentSubmitter = parentBoard != null
                    && parentBoard.getCategory().isSubmit()
                    && parentBoard.isOwner(currentMember.getId());
            return isLeader || isParentSubmitter;
        }
        return isSubmitter || isLeader;
    }

    private void validateAlreadySubmittedAssignmentThisWeek(ActivityGroupBoardCategory category, Long parentId, String memberId) {
        if (category.isSubmit()) {
            boolean hasSubmitted = activityGroupBoardRepository.existsByParentIdAndCategoryAndMemberId(parentId, ActivityGroupBoardCategory.SUBMIT, memberId);
            if (hasSubmitted) {
                throw new AlreadySubmittedThisWeekAssignmentException();
            }
        }
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupBoardResponseDto> getAllActivityGroupBoard(Pageable pageable) {
        Page<ActivityGroupBoard> boards = activityGroupBoardRepository.findAll(pageable);
        return new PagedResponseDto<>(boards.map(board -> {
            MemberBasicInfoDto memberBasicInfoDto = externalRetrieveMemberUseCase.getMemberBasicInfoById(board.getMemberId());
            return ActivityGroupBoardResponseDto.toDto(board, memberBasicInfoDto);
        }));
    }

    @Transactional(readOnly = true)
    public ActivityGroupBoardResponseDto getActivityGroupBoardById(Long activityGroupBoardId) throws PermissionDeniedException {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroupBoard board = getActivityGroupBoardByIdOrThrow(activityGroupBoardId);
        if (!activityGroupMemberService.isGroupMember(board.getActivityGroup(), currentMember.getId())) {
            throw new PermissionDeniedException("해당 활동 그룹의 멤버가 아닙니다.");
        }
        if (!hasAccessToBoard(board.getActivityGroup(), board, currentMember)) {
            throw new PermissionDeniedException("해당 게시물을 조회할 권한이 없습니다.");
        }
        MemberBasicInfoDto memberBasicInfoDto = externalRetrieveMemberUseCase.getMemberBasicInfoById(board.getMemberId());
        return ActivityGroupBoardResponseDto.toDto(board, memberBasicInfoDto);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupBoardResponseDto> getActivityGroupBoardByCategory(Long activityGroupId, ActivityGroupBoardCategory category, Pageable pageable) throws PermissionDeniedException {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);
        if (!activityGroupMemberService.isGroupMember(activityGroup, currentMember.getId())) {
            throw new PermissionDeniedException("해당 활동 그룹의 멤버가 아닙니다.");
        }
        Page<ActivityGroupBoard> boards = activityGroupBoardRepository.findAllByActivityGroup_IdAndCategory(activityGroupId, category, pageable);
        List<ActivityGroupBoardResponseDto> filteredBoards = boards.stream()
                .filter(board -> hasAccessToBoard(board.getActivityGroup(), board, currentMember))
                .map(board -> {
                    MemberBasicInfoDto memberBasicInfoDto = externalRetrieveMemberUseCase.getMemberBasicInfoById(board.getMemberId());
                    return ActivityGroupBoardResponseDto.toDto(board, memberBasicInfoDto);
                })
                .toList();
        return new PagedResponseDto<>(new PageImpl<>(filteredBoards, pageable, boards.getTotalElements()));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupBoardChildResponseDto> getActivityGroupBoardByParent(Long parentId, Pageable pageable) throws PermissionDeniedException {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroupBoard parentBoard = getActivityGroupBoardByIdOrThrow(parentId);
        Long activityGroupId = parentBoard.getActivityGroup().getId();
        if (!activityGroupMemberService.isGroupMember(parentBoard.getActivityGroup(), currentMember.getId())) {
            throw new PermissionDeniedException("해당 활동 그룹의 멤버가 아닙니다.");
        }

        GroupMember groupLeader = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(activityGroupId, ActivityGroupRole.LEADER);
        parentBoard.validateAccessPermission(currentMember, groupLeader);

        List<ActivityGroupBoard> childBoards = getChildBoards(parentId);
        List<ActivityGroupBoardChildResponseDto> filteredBoards = childBoards.stream()
                .filter(board -> hasAccessToBoard(board.getActivityGroup(), board, currentMember))
                .map(this::toActivityGroupBoardChildResponseDtoWithMemberInfo)
                .toList();

        return new PagedResponseDto<>(new PageImpl<>(filteredBoards, pageable, filteredBoards.size()));
    }

    @Transactional(readOnly = true)
    public List<AssignmentSubmissionWithFeedbackResponseDto> getMyAssignmentsWithFeedbacks(Long parentId) {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();

        List<ActivityGroupBoard> mySubmissions = activityGroupBoardRepository.findMySubmissionsWithFeedbacks(parentId, currentMember.getId());
        return mySubmissions.stream()
                .map(submission -> {
                    List<FeedbackResponseDto> feedbackDtos = submission.getChildren().stream()
                            .filter(ActivityGroupBoard::isFeedback)
                            .map(board ->  {
                                MemberBasicInfoDto memberBasicInfoDto = externalRetrieveMemberUseCase.getMemberBasicInfoById(board.getMemberId());
                                return FeedbackResponseDto.toDto(board, memberBasicInfoDto);
                            })
                            .toList();
                    MemberBasicInfoDto memberBasicInfoDto = externalRetrieveMemberUseCase.getMemberBasicInfoById(submission.getMemberId());
                    return AssignmentSubmissionWithFeedbackResponseDto.toDto(submission, memberBasicInfoDto, feedbackDtos);
                })
                .toList();
    }

    @Transactional
    public ActivityGroupBoardReferenceDto updateActivityGroupBoard(Long activityGroupBoardId, ActivityGroupBoardUpdateRequestDto requestDto) throws PermissionDeniedException {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroupBoard board = getActivityGroupBoardByIdOrThrow(activityGroupBoardId);
        board.validateAccessPermission(currentMember);

        board.update(requestDto, uploadedFileService);
        activityGroupBoardRepository.save(board);
        Long parentId = (board.getParent() != null) ? board.getParent().getId() : null;
        return ActivityGroupBoardReferenceDto.toDto(board.getId(), board.getActivityGroup().getId(), parentId);
    }

    public ActivityGroupBoardReferenceDto deleteActivityGroupBoard(Long activityGroupBoardId) throws PermissionDeniedException {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroupBoard board = getActivityGroupBoardByIdOrThrow(activityGroupBoardId);
        board.validateAccessPermission(currentMember);
        activityGroupBoardRepository.delete(board);
        Long parentId = (board.getParent() != null) ? board.getParent().getId() : null;
        return ActivityGroupBoardReferenceDto.toDto(board.getId(), board.getActivityGroup().getId(), parentId);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupBoardResponseDto> getDeletedActivityGroupBoards(Pageable pageable) {
        Page<ActivityGroupBoard> boards = activityGroupBoardRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(boards.map(board -> {
            MemberBasicInfoDto memberBasicInfoDto = externalRetrieveMemberUseCase.getMemberBasicInfoById(board.getMemberId());
            return ActivityGroupBoardResponseDto.toDto(board, memberBasicInfoDto);
        }));
    }

    private ActivityGroupBoard getActivityGroupBoardByIdOrThrow(Long activityGroupBoardId) {
        return activityGroupBoardRepository.findById(activityGroupBoardId)
                .orElseThrow(() -> new NotFoundException("해당 게시글을 찾을 수 없습니다."));
    }

    private List<ActivityGroupBoard> getChildBoards(Long activityGroupBoardId) {
        List<ActivityGroupBoard> children = activityGroupBoardRepository.findAllChildrenByParentId(activityGroupBoardId);
        children.sort(Comparator.comparing(ActivityGroupBoard::getCreatedAt).reversed());
        return children;
    }

    public ActivityGroupBoardChildResponseDto toActivityGroupBoardChildResponseDtoWithMemberInfo(ActivityGroupBoard activityGroupBoard) {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        MemberBasicInfoDto memberBasicInfo = externalRetrieveMemberUseCase.getMemberBasicInfoById(activityGroupBoard.getMemberId());
        List<ActivityGroupBoardChildResponseDto> childrenDtos = activityGroupBoard.getChildren().stream()
                .filter(children -> hasAccessToBoard(children.getActivityGroup(), children, currentMember))
                .map(this::toActivityGroupBoardChildResponseDtoWithMemberInfo)
                .toList();
        return ActivityGroupBoardChildResponseDto.toDto(activityGroupBoard, memberBasicInfo, childrenDtos);
    }

    private void validateParentBoard(ActivityGroupBoardCategory category, Long parentId) throws InvalidParentBoardException {
        if ((category.isNotice() || category.isWeeklyActivity())) {
            if (parentId != null) {
                throw new InvalidParentBoardException(category.getDescription() + " 게시물은 부모 게시판을 가질 수 없습니다.");
            } else {
                return;
            }
        }

        if ((category.isAssignment() || category.isSubmit() || category.isFeedback()) && parentId == null) {
            throw new InvalidParentBoardException(category.getDescription() + " 게시물은 부모 게시판이 필요합니다.");
        }

        ActivityGroupBoard parentBoard = getActivityGroupBoardByIdOrThrow(parentId);

        ActivityGroupBoardCategory expectedParentCategory = switch (category) {
            case ASSIGNMENT -> ActivityGroupBoardCategory.WEEKLY_ACTIVITY;
            case SUBMIT -> ActivityGroupBoardCategory.ASSIGNMENT;
            case FEEDBACK -> ActivityGroupBoardCategory.SUBMIT;
            default -> throw new InvalidParentBoardException("유효하지 않은 카테고리입니다.");
        };

        if (parentBoard.getCategory() != expectedParentCategory) {
            String message = switch (category) {
                case ASSIGNMENT -> "과제의 부모 게시판은 주차별 활동 게시판이어야 합니다.";
                case SUBMIT -> "제출의 부모 게시판은 과제 게시판이어야 합니다.";
                case FEEDBACK -> "피드백의 부모 게시판은 제출 게시판이어야 합니다.";
                default -> "유효하지 않은 카테고리입니다.";
            };
            throw new InvalidParentBoardException(message);
        }
    }

    private void notifyMembersAboutNewBoard(Long activityGroupId, ActivityGroup activityGroup, Member member) {
        GroupMember groupMember = activityGroupMemberService.getGroupMemberByActivityGroupAndMemberOrThrow(activityGroup, member.getId());
        if (groupMember.isLeader()) {
            List<GroupMember> groupMembers = activityGroupMemberService.getGroupMemberByActivityGroupId(activityGroupId);
            groupMembers
                    .forEach(gMember -> {
                        if (!gMember.isOwner(member.getId())) {
                            externalSendNotificationUseCase.sendNotificationToMember(gMember.getMemberId(), "[" + activityGroup.getName() + "] " + member.getName() + "님이 새 게시글을 등록하였습니다.");
                        }
                    });
        } else {
            GroupMember groupLeader = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(activityGroupId, ActivityGroupRole.LEADER);
            if (groupLeader != null) {
                externalSendNotificationUseCase.sendNotificationToMember(groupLeader.getMemberId(), "[" + activityGroup.getName() + "] " + member.getName() + "님이 새 게시글을 등록하였습니다.");
            }
        }
    }
}
