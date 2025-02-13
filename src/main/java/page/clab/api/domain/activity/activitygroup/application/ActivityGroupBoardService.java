package page.clab.api.domain.activity.activitygroup.application;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import page.clab.api.domain.activity.activitygroup.dao.ActivityGroupBoardRepository;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoardCategory;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupRole;
import page.clab.api.domain.activity.activitygroup.domain.GroupMember;
import page.clab.api.domain.activity.activitygroup.domain.GroupMemberStatus;
import page.clab.api.domain.activity.activitygroup.dto.mapper.ActivityGroupDtoMapper;
import page.clab.api.domain.activity.activitygroup.dto.request.ActivityGroupBoardRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.request.ActivityGroupBoardUpdateRequestDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupBoardChildResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupBoardReferenceDto;
import page.clab.api.domain.activity.activitygroup.dto.response.ActivityGroupBoardResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.AssignmentSubmissionWithFeedbackResponseDto;
import page.clab.api.domain.activity.activitygroup.dto.response.FeedbackResponseDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.domain.memberManagement.member.domain.Role;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.external.memberManagement.notification.application.port.ExternalSendNotificationUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.file.application.UploadedFileService;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class ActivityGroupBoardService {

    private final ActivityGroupBoardRepository activityGroupBoardRepository;
    private final ActivityGroupAdminService activityGroupAdminService;
    private final ActivityGroupMemberService activityGroupMemberService;
    private final UploadedFileService uploadedFileService;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalSendNotificationUseCase externalSendNotificationUseCase;
    private final ActivityGroupDtoMapper mapper;

    /**
     * 새로운 활동 그룹 게시판을 생성합니다.
     *
     * <p>새 게시판을 생성하기 전 다음 검증이 수행됩니다:</p>
     * - 현재 사용자가 해당 활동 그룹의 멤버인지 확인 - 공지, 주차별 활동, 과제, 피드백 카테고리는 관리자 또는 리더 권한 필요 - 부모 게시판의 유효성 확인 - 이번 주에 이미 과제를 제출했는지 확인
     *
     * <p>부모 게시판이 있을 경우 자식 게시판으로 추가하며, 게시판 생성 후 알림을 전송합니다.</p>
     *
     * @param parentId        부모 게시판의 ID (없을 수 있음)
     * @param activityGroupId 활동 그룹의 ID
     * @param requestDto      게시판 생성 요청 DTO
     * @return ActivityGroupBoardReferenceDto
     * @throws BaseException {@code ErrorCode.PERMISSION_DENIED} - 권한이 없는 경우 예외 발생
     */
    @Transactional
    public ActivityGroupBoardReferenceDto createActivityGroupBoard(Long parentId, Long activityGroupId,
        ActivityGroupBoardRequestDto requestDto) {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupById(activityGroupId);

        validateGroupMember(activityGroup, currentMember);
        validateCanCreateBoard(activityGroup, requestDto.getCategory(), currentMember);
        validateParentBoard(requestDto.getCategory(), parentId);
        validateAlreadySubmittedAssignmentThisWeek(requestDto.getCategory(), parentId, currentMember.getId());

        List<UploadedFile> uploadedFiles = uploadedFileService.getUploadedFilesByUrls(requestDto.getFileUrls());

        ActivityGroupBoard parentBoard = parentId != null ? getActivityGroupBoardById(parentId) : null;
        ActivityGroupBoard board = mapper.fromDto(requestDto, currentMember, activityGroup, parentBoard, uploadedFiles);
        board.validateEssentialElementByCategory();

        if (parentId != null) {
            parentBoard.addChild(board);
            activityGroupBoardRepository.save(parentBoard);
        }
        activityGroupBoardRepository.save(board);

        notifyMembersAboutNewBoard(activityGroupId, activityGroup, board, currentMember);
        return mapper.of(board.getId(), activityGroupId, parentId);
    }

    private void validateGroupMember(ActivityGroup activityGroup, Member currentMember) {
        if (!activityGroupMemberService.isGroupMember(activityGroup, currentMember.getId())) {
            throw new BaseException(ErrorCode.PERMISSION_DENIED, "해당 활동 그룹의 멤버가 아닙니다.");
        }
    }

    private void validateCanCreateBoard(ActivityGroup activityGroup, ActivityGroupBoardCategory category,
        Member currentMember) {
        Role role = currentMember.getRole();
        boolean isRequireAdminOrLeaderCategory = category.isNotice() ||
            category.isWeeklyActivity() ||
            category.isAssignment() ||
            category.isFeedback();

        // NOTICE, WEEKLY_ACTIVITY, ASSIGNMENT, FEEDBACK 카테고리에서 권한이 ADMIN 이상이 아니거나, 리더가 아니면 예외처리
        if (isRequireAdminOrLeaderCategory && !(role.isHigherThanOrEqual(Role.ADMIN)
            || activityGroupAdminService.hasLeaderOrAdminRole(activityGroup, currentMember))) {
            throw new BaseException(ErrorCode.PERMISSION_DENIED, "해당 카테고리에서 게시글을 작성할 권한이 없습니다.");
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
        boolean isLeader = activityGroupAdminService.hasLeaderOrAdminRole(activityGroup, currentMember);
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

    private void validateAlreadySubmittedAssignmentThisWeek(ActivityGroupBoardCategory category, Long parentId,
        String memberId) {
        if (category.isSubmit()) {
            boolean hasSubmitted = activityGroupBoardRepository.existsByParentIdAndCategoryAndMemberId(parentId,
                ActivityGroupBoardCategory.SUBMIT, memberId);
            if (hasSubmitted) {
                throw new BaseException(ErrorCode.ALREADY_SUBMITTED_THIS_WEEK_ASSIGNMENT);
            }
        }
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupBoardResponseDto> getAllActivityGroupBoard(Pageable pageable) {
        Page<ActivityGroupBoard> boards = activityGroupBoardRepository.findAll(pageable);
        return new PagedResponseDto<>(boards.map(board -> {
            MemberBasicInfoDto memberBasicInfoDto = externalRetrieveMemberUseCase.getMemberBasicInfoById(
                board.getMemberId());
            return mapper.toBoardDto(board, memberBasicInfoDto);
        }));
    }

    @Transactional(readOnly = true)
    public ActivityGroupBoardResponseDto getActivityGroupBoard(Long activityGroupBoardId) {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroupBoard board = getActivityGroupBoardById(activityGroupBoardId);
        validateGroupMember(board.getActivityGroup(), currentMember);
        if (!hasAccessToBoard(board.getActivityGroup(), board, currentMember)) {
            throw new BaseException(ErrorCode.PERMISSION_DENIED, "해당 게시물을 조회할 권한이 없습니다.");
        }
        MemberBasicInfoDto memberBasicInfoDto = externalRetrieveMemberUseCase.getMemberBasicInfoById(
            board.getMemberId());
        return mapper.toBoardDto(board, memberBasicInfoDto);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupBoardResponseDto> getActivityGroupBoardByCategory(Long activityGroupId,
        ActivityGroupBoardCategory category, Pageable pageable) {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupById(activityGroupId);
        validateGroupMember(activityGroup, currentMember);
        Page<ActivityGroupBoard> boards = activityGroupBoardRepository.findAllByActivityGroup_IdAndCategory(
            activityGroupId, category, pageable);
        // 사용자 권한에 따라 접근 가능한 게시판만 반환합니다.
        List<ActivityGroupBoardResponseDto> filteredBoards = boards.stream()
            .filter(board -> hasAccessToBoard(board.getActivityGroup(), board, currentMember))
            .map(board -> {
                MemberBasicInfoDto memberBasicInfoDto = externalRetrieveMemberUseCase.getMemberBasicInfoById(
                    board.getMemberId());
                return mapper.toBoardDto(board, memberBasicInfoDto);
            })
            .toList();
        return new PagedResponseDto<>(new PageImpl<>(filteredBoards, pageable, boards.getTotalElements()));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupBoardChildResponseDto> getActivityGroupBoardByParent(Long parentId,
        Pageable pageable) {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroupBoard parentBoard = getActivityGroupBoardById(parentId);
        Long activityGroupId = parentBoard.getActivityGroup().getId();
        validateGroupMember(parentBoard.getActivityGroup(), currentMember);

        List<GroupMember> groupLeaders = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(
            activityGroupId, ActivityGroupRole.LEADER);
        parentBoard.validateAccessPermission(currentMember, groupLeaders);

        List<ActivityGroupBoard> childBoards = getChildBoards(parentId);
        // 접근 가능한 자식 게시판만 조회합니다.
        List<ActivityGroupBoardChildResponseDto> filteredBoards = childBoards.stream()
            .filter(board -> hasAccessToBoard(board.getActivityGroup(), board, currentMember))
            .map(this::toActivityGroupBoardChildResponseDtoWithMemberInfo)
            .toList();

        return new PagedResponseDto<>(new PageImpl<>(filteredBoards, pageable, filteredBoards.size()));
    }

    @Transactional(readOnly = true)
    public List<AssignmentSubmissionWithFeedbackResponseDto> getMyAssignmentsWithFeedbacks(Long parentId) {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();

        List<ActivityGroupBoard> mySubmissions = activityGroupBoardRepository.findMySubmissionsWithFeedbacks(parentId,
            currentMember.getId());
        // 해당 과제에 대한 피드백 목록을 조회합니다.
        return mySubmissions.stream()
            .map(submission -> {
                List<FeedbackResponseDto> feedbackDtos = submission.getChildren().stream()
                    .filter(ActivityGroupBoard::isFeedback)
                    .map(board -> {
                        MemberBasicInfoDto memberBasicInfoDto = externalRetrieveMemberUseCase.getMemberBasicInfoById(
                            board.getMemberId());
                        return mapper.toFeedbackDto(board, memberBasicInfoDto);
                    })
                    .toList();
                MemberBasicInfoDto memberBasicInfoDto = externalRetrieveMemberUseCase.getMemberBasicInfoById(
                    submission.getMemberId());
                return mapper.toAssignmentDto(submission, memberBasicInfoDto, feedbackDtos);
            })
            .toList();
    }

    @Transactional
    public ActivityGroupBoardReferenceDto updateActivityGroupBoard(Long activityGroupBoardId,
        ActivityGroupBoardUpdateRequestDto requestDto) {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroupBoard board = getActivityGroupBoardById(activityGroupBoardId);
        board.validateAccessPermission(currentMember);

        board.update(requestDto, uploadedFileService);
        activityGroupBoardRepository.save(board);
        Long parentId = (board.getParent() != null) ? board.getParent().getId() : null;
        return mapper.of(board.getId(), board.getActivityGroup().getId(), parentId);
    }

    public ActivityGroupBoardReferenceDto deleteActivityGroupBoard(Long activityGroupBoardId) {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        ActivityGroupBoard board = getActivityGroupBoardById(activityGroupBoardId);
        board.validateAccessPermission(currentMember);
        activityGroupBoardRepository.delete(board);
        Long parentId = (board.getParent() != null) ? board.getParent().getId() : null;
        return mapper.of(board.getId(), board.getActivityGroup().getId(), parentId);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupBoardResponseDto> getDeletedActivityGroupBoards(Pageable pageable) {
        Page<ActivityGroupBoard> boards = activityGroupBoardRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(boards.map(board -> {
            MemberBasicInfoDto memberBasicInfoDto = externalRetrieveMemberUseCase.getMemberBasicInfoById(
                board.getMemberId());
            return mapper.toBoardDto(board, memberBasicInfoDto);
        }));
    }

    public ActivityGroupBoard getActivityGroupBoardById(Long activityGroupBoardId) {
        return activityGroupBoardRepository.findById(activityGroupBoardId)
            .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND, "해당 활동 그룹 게시글을 찾을 수 없습니다."));
    }

    private List<ActivityGroupBoard> getChildBoards(Long activityGroupBoardId) {
        List<ActivityGroupBoard> children = activityGroupBoardRepository.findAllChildrenByParentId(
            activityGroupBoardId);
        children.sort(Comparator.comparing(ActivityGroupBoard::getCreatedAt).reversed());
        return children;
    }

    public ActivityGroupBoardChildResponseDto toActivityGroupBoardChildResponseDtoWithMemberInfo(
        ActivityGroupBoard activityGroupBoard) {
        Member currentMember = externalRetrieveMemberUseCase.getCurrentMember();
        MemberBasicInfoDto memberBasicInfo = externalRetrieveMemberUseCase.getMemberBasicInfoById(
            activityGroupBoard.getMemberId());
        List<ActivityGroupBoardChildResponseDto> childrenDtos = activityGroupBoard.getChildren().stream()
            .filter(children -> hasAccessToBoard(children.getActivityGroup(), children, currentMember))
            .map(this::toActivityGroupBoardChildResponseDtoWithMemberInfo)
            .toList();
        return mapper.toChildDto(activityGroupBoard, memberBasicInfo, childrenDtos);
    }

    /**
     * 주어진 카테고리와 부모 게시판의 유효성을 검증합니다.
     *
     * <p>다음 규칙을 따릅니다:</p>
     * - 공지와 주차별 활동 게시판은 부모 게시판을 가질 수 없습니다. - 과제, 제출, 피드백 게시판은 부모 게시판이 반드시 필요합니다. - 과제의 부모는 주차별 활동 게시판이어야 합니다. - 제출의 부모는
     * 과제 게시판이어야 합니다. - 피드백의 부모는 제출 게시판이어야 합니다.
     *
     * @param category 게시판 카테고리
     * @param parentId 부모 게시판의 ID
     * @throws BaseException {@code ErrorCode.INVALID_PARENT_BOARD} 부모 게시판이 유효하지 않은 경우 예외 발생
     */
    private void validateParentBoard(ActivityGroupBoardCategory category, Long parentId) {
        if ((category.isNotice() || category.isWeeklyActivity())) {
            if (parentId != null) {
                throw new BaseException(ErrorCode.INVALID_PARENT_BOARD,
                    category.getDescription() + " 게시물은 부모 게시판을 가질 수 없습니다.");
            } else {
                return;
            }
        }

        if ((category.isAssignment() || category.isSubmit() || category.isFeedback()) && parentId == null) {
            throw new BaseException(ErrorCode.INVALID_PARENT_BOARD, category.getDescription() + " 게시물은 부모 게시판이 필요합니다.");
        }

        ActivityGroupBoard parentBoard = getActivityGroupBoardById(parentId);

        ActivityGroupBoardCategory expectedParentCategory = switch (category) {
            case ASSIGNMENT -> ActivityGroupBoardCategory.WEEKLY_ACTIVITY;
            case SUBMIT -> ActivityGroupBoardCategory.ASSIGNMENT;
            case FEEDBACK -> ActivityGroupBoardCategory.SUBMIT;
            default -> throw new BaseException(ErrorCode.INVALID_PARENT_BOARD, "유효하지 않은 카테고리입니다.");
        };

        if (parentBoard.getCategory() != expectedParentCategory) {
            String message = switch (category) {
                case ASSIGNMENT -> "과제의 부모 게시판은 주차별 활동 게시판이어야 합니다.";
                case SUBMIT -> "제출의 부모 게시판은 과제 게시판이어야 합니다.";
                case FEEDBACK -> "피드백의 부모 게시판은 제출 게시판이어야 합니다.";
                default -> "유효하지 않은 카테고리입니다.";
            };
            throw new BaseException(ErrorCode.INVALID_PARENT_BOARD, message);
        }
    }

    /**
     * 새 게시판 생성에 대한 알림을 활동 그룹 멤버들에게 전송합니다.
     *
     * <p>이 메서드는 활동 그룹에서 새 게시판이 생성될 때 관련 멤버들에게 알림을 전송합니다.
     * 만약 게시판을 생성한 멤버가 그룹 리더일 경우, 본인을 제외한 다른 모든 멤버들에게 알림이 전송됩니다. 단, 게시판이 피드백 유형일 경우에는 과제 제출한 멤버에게만 알림이 전송됩니다. 만약 게시판을
     * 생성한 멤버가 팀원인 경우, 그룹 리더에게만 알림이 전송됩니다.</p>
     *
     * @param activityGroupId 게시판이 생성된 활동 그룹의 ID
     * @param activityGroup   게시판이 생성된 활동 그룹
     * @param board           생성된 게시판 객체
     * @param member          게시판을 생성한 멤버 객체
     */
    private void notifyMembersAboutNewBoard(Long activityGroupId, ActivityGroup activityGroup, ActivityGroupBoard board,
        Member member) {
        GroupMember groupMember = activityGroupMemberService.getGroupMemberByActivityGroupAndMember(activityGroup,
            member.getId());
        if (groupMember.isLeader()) {
            if (board.isFeedback()) {
                String submitMemberId = board.getParent().getMemberId();
                externalSendNotificationUseCase.sendNotificationToMember(submitMemberId,
                    "[" + activityGroup.getName() + "] " + member.getName() + "님이 새 피드백을 등록하였습니다.");
            } else {
                List<GroupMember> groupMembers = activityGroupMemberService.getGroupMemberByActivityGroupIdAndStatus(
                    activityGroupId, GroupMemberStatus.ACCEPTED);
                List<String> groupMembersId = groupMembers.stream()
                    .map(GroupMember::getMemberId)
                    .filter(memberId -> !memberId.equals(groupMember.getMemberId()))
                    .collect(Collectors.toList());
                externalSendNotificationUseCase.sendNotificationToMembers(groupMembersId,
                    "[" + activityGroup.getName() + "] " + member.getName() + "님이 새 " + board.getCategory()
                        .getDescription() + "을(를) 등록하였습니다.");
            }
        } else {
            List<GroupMember> groupLeaders = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(
                activityGroupId, ActivityGroupRole.LEADER);
            if (!CollectionUtils.isEmpty(groupLeaders)) {
                groupLeaders.forEach(
                    leader -> externalSendNotificationUseCase.sendNotificationToMember(leader.getMemberId(),
                        "[" + activityGroup.getName() + "] " + member.getName() + "님이 새 " + board.getCategory()
                            .getDescription() + "을(를) 등록하였습니다."));
            }
        }
    }
}
