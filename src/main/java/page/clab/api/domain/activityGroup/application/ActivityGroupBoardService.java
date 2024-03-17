package page.clab.api.domain.activityGroup.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.activityGroup.dao.ActivityGroupBoardRepository;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoardCategory;
import page.clab.api.domain.activityGroup.domain.ActivityGroupRole;
import page.clab.api.domain.activityGroup.domain.GroupMember;
import page.clab.api.domain.activityGroup.dto.request.ActivityGroupBoardRequestDto;
import page.clab.api.domain.activityGroup.dto.request.ActivityGroupBoardUpdateRequestDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupBoardChildResponseDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupBoardResponseDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupBoardUpdateResponseDto;
import page.clab.api.domain.activityGroup.dto.response.AssignmentSubmissionWithFeedbackResponseDto;
import page.clab.api.domain.activityGroup.dto.response.FeedbackResponseDto;
import page.clab.api.domain.activityGroup.exception.InvalidParentBoardException;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.file.application.FileService;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityGroupBoardService {

    private final ActivityGroupBoardRepository activityGroupBoardRepository;

    private final MemberService memberService;

    private final ActivityGroupAdminService activityGroupAdminService;

    private final ActivityGroupMemberService activityGroupMemberService;

    private final NotificationService notificationService;

    private final FileService fileService;

    @Transactional
    public Long createActivityGroupBoard(Long parentId, Long activityGroupId, ActivityGroupBoardRequestDto activityGroupBoardRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);
        if (!activityGroupMemberService.isGroupMember(activityGroup, member)) {
            throw new PermissionDeniedException("활동 그룹 멤버만 게시글을 등록할 수 있습니다.");
        }
        ActivityGroupBoard board = ActivityGroupBoard.of(activityGroupBoardRequestDto);
        validateParentBoard(activityGroupBoardRequestDto.getCategory(), parentId);
        board.setMember(member);
        board.setActivityGroup(activityGroup);
        if (parentId != null) {
            ActivityGroupBoard parentBoard = getActivityGroupBoardByIdOrThrow(parentId);
            board.setParent(parentBoard);
            parentBoard.getChildren().add(board);
            activityGroupBoardRepository.save(parentBoard);
        }

        List<String> fileUrls = activityGroupBoardRequestDto.getFileUrls();
        if (fileUrls != null) {
            List<UploadedFile> uploadFileList =  fileUrls.stream()
                    .map(fileService::getUploadedFileByUrl)
                    .collect(Collectors.toList());
            board.setUploadedFiles(uploadFileList);
        }
        Long id = activityGroupBoardRepository.save(board).getId();

        GroupMember groupMember = activityGroupMemberService.getGroupMemberByActivityGroupAndMemberOrThrow(activityGroup, member);
        if (groupMember.getRole() == ActivityGroupRole.LEADER) {
            List<GroupMember> groupMembers = activityGroupMemberService.getGroupMemberByActivityGroupId(activityGroupId);
            groupMembers
                    .forEach(gMember -> {
                        if (!Objects.equals(gMember.getMember().getId(), member.getId())) {
                            notificationService.sendNotificationToMember(
                                    gMember.getMember(),
                                    "[" + activityGroup.getName() + "] " + member.getName() + "님이 새 게시글을 등록하였습니다."
                            );
                        }
                    });
        } else {
            GroupMember groupLeader = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(activityGroupId, ActivityGroupRole.LEADER);
            if (groupLeader != null) {
                notificationService.sendNotificationToMember(
                        groupLeader.getMember(),
                        "[" + activityGroup.getName() + "] " + member.getName() + "님이 새 게시글을 등록하였습니다."
                );
            }
        }
        return id;
    }

    public PagedResponseDto<ActivityGroupBoardResponseDto> getAllActivityGroupBoard(Pageable pageable) {
        Page<ActivityGroupBoard> boards = activityGroupBoardRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(boards.map(ActivityGroupBoardResponseDto::of));
    }

    public ActivityGroupBoardResponseDto getActivityGroupBoardById(Long activityGroupBoardId) {
        ActivityGroupBoard board = getActivityGroupBoardByIdOrThrow(activityGroupBoardId);
        return ActivityGroupBoardResponseDto.of(board);
    }

    public PagedResponseDto<ActivityGroupBoardResponseDto> getActivityGroupBoardByCategory(Long activityGroupId, ActivityGroupBoardCategory category, Pageable pageable) {
        Page<ActivityGroupBoard> boards = activityGroupBoardRepository.findAllByActivityGroup_IdAndCategoryOrderByCreatedAtDesc(activityGroupId, category, pageable);
        return new PagedResponseDto<>(boards.map(ActivityGroupBoardResponseDto::of));
    }

    public PagedResponseDto<ActivityGroupBoardChildResponseDto> getActivityGroupBoardByParent(Long parentId, Pageable pageable) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        ActivityGroupBoard parentBoard = getActivityGroupBoardByIdOrThrow(parentId);
        Long activityGroupId = parentBoard.getActivityGroup().getId();
        GroupMember leader = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(activityGroupId, ActivityGroupRole.LEADER);

        if (!member.isAdminRole() &&
                leader != null &&
                !leader.getMember().getId()
                        .equals(member.getId())
        ) {
            if (parentBoard.getCategory().equals(ActivityGroupBoardCategory.ASSIGNMENT)) {
                throw new PermissionDeniedException("제출 카테고리 게시물 전체는 그룹의 리더 또는 관리자만 열람할 수 있습니다.");
            }
        }

        List<ActivityGroupBoard> boardList = getChildBoards(parentId);
        Page<ActivityGroupBoard> boardPage = new PageImpl<>(boardList, pageable, boardList.size());
        return new PagedResponseDto<>(boardPage.map(ActivityGroupBoardChildResponseDto::of));
    }

    @Transactional
    public List<AssignmentSubmissionWithFeedbackResponseDto> getMyAssignmentsWithFeedbacks(Long parentId) {
        Member currentMember = memberService.getCurrentMember();
        ActivityGroupBoard parentBoard = getActivityGroupBoardByIdOrThrow(parentId);
        List<ActivityGroupBoard> mySubmissions = parentBoard.getChildren().stream()
                .filter(child -> child.getCategory() == ActivityGroupBoardCategory.SUBMIT && child.getMember().equals(currentMember))
                .toList();
        List<AssignmentSubmissionWithFeedbackResponseDto> submissionsWithFeedbacks = new ArrayList<>();
        for (ActivityGroupBoard submission : mySubmissions) {
            AssignmentSubmissionWithFeedbackResponseDto submissionDto = AssignmentSubmissionWithFeedbackResponseDto.of(submission);
            List<FeedbackResponseDto> feedbackDtos = submission.getChildren().stream()
                    .filter(feedback -> feedback.getCategory() == ActivityGroupBoardCategory.FEEDBACK)
                    .map(FeedbackResponseDto::of)
                    .toList();
            submissionDto.setFeedbacks(feedbackDtos);
            submissionsWithFeedbacks.add(submissionDto);
        }
        return submissionsWithFeedbacks;
    }

    public ActivityGroupBoardUpdateResponseDto updateActivityGroupBoard(Long activityGroupBoardId, ActivityGroupBoardUpdateRequestDto activityGroupBoardUpdateRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        ActivityGroupBoard board = getActivityGroupBoardByIdOrThrow(activityGroupBoardId);
        if (!member.getId().equals(board.getMember().getId()) && !member.isAdminRole()) {
            throw new PermissionDeniedException("활동 그룹 게시판 작성자 또는 운영진만 수정할 수 있습니다.");
        }
        board.update(activityGroupBoardUpdateRequestDto, fileService);
        board.setUpdateTime(LocalDateTime.now());
        return ActivityGroupBoardUpdateResponseDto.builder()
                .id(activityGroupBoardRepository.save(board).getId())
                .parentId(board.getParent() != null ? board.getParent().getId() : null)
                .build();
    }

    public Long deleteActivityGroupBoard(Long activityGroupBoardId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        ActivityGroupBoard board = getActivityGroupBoardByIdOrThrow(activityGroupBoardId);

        if (!member.getId().equals(board.getMember().getId()) && !member.isAdminRole()) {
            throw new PermissionDeniedException("활동 그룹 게시판 작성자 또는 운영진만 삭제할 수 있습니다.");
        }

        activityGroupBoardRepository.delete(board);
        return board.getId();
    }

    private ActivityGroupBoard getActivityGroupBoardByIdOrThrow(Long activityGroupBoardId) {
        return activityGroupBoardRepository.findById(activityGroupBoardId)
                .orElseThrow(() -> new NotFoundException("해당 게시글을 찾을 수 없습니다."));
    }

    private List<ActivityGroupBoard> getChildBoards(Long activityGroupBoardId) {
        List<ActivityGroupBoard> boardList = new ArrayList<>();
        ActivityGroupBoard board = getActivityGroupBoardByIdOrThrow(activityGroupBoardId);
        if (board.getParent() == null || board.getChildren() != null) {
            boardList.add(board);
            for (ActivityGroupBoard child : board.getChildren()) {
                boardList.addAll(getChildBoards(child.getId()));
            }
        } else {
            boardList.add(board);
        }
        boardList.sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));
        return boardList;
    }

    private void validateParentBoard(ActivityGroupBoardCategory category, Long parentId) throws InvalidParentBoardException {
        if (category == ActivityGroupBoardCategory.ASSIGNMENT || category == ActivityGroupBoardCategory.SUBMIT || category == ActivityGroupBoardCategory.FEEDBACK) {
            if (parentId == null) {
                throw new InvalidParentBoardException(category.getDescription() + "는 부모 게시판을 가져야 합니다.");
            }
            ActivityGroupBoard parentBoard = getActivityGroupBoardByIdOrThrow(parentId);
            switch (category) {
                case ASSIGNMENT:
                    if (parentBoard.getCategory() != ActivityGroupBoardCategory.WEEKLY_ACTIVITY) {
                        throw new InvalidParentBoardException("과제의 부모 게시판은 주차별활동 게시판이어야 합니다.");
                    }
                    break;
                case SUBMIT:
                    if (parentBoard.getCategory() != ActivityGroupBoardCategory.ASSIGNMENT) {
                        throw new InvalidParentBoardException("제출의 부모 게시판은 과제 게시판이어야 합니다.");
                    }
                    break;
                case FEEDBACK:
                    if (parentBoard.getCategory() != ActivityGroupBoardCategory.SUBMIT) {
                        throw new InvalidParentBoardException("피드백의 부모 게시판은 제출 게시판이어야 합니다.");
                    }
                    break;
                default:
                    break;
            }
        } else if (parentId != null) {
            throw new InvalidParentBoardException("공지사항과 주차별활동의 부모 게시판은 존재할 수 없습니다.");
        }
    }

}