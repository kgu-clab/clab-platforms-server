package page.clab.api.domain.activityGroup.application;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupBoardChildResponseDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupBoardResponseDto;
import page.clab.api.domain.activityGroup.exception.NotSubmitCategoryBoardException;
import page.clab.api.domain.activityPhoto.dto.response.ActivityPhotoResponseDto;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.domain.notification.dto.request.NotificationRequestDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.file.application.FileService;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

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
    public Long createActivityGroupBoard(Long parentId, Long activityGroupId, ActivityGroupBoardRequestDto activityGroupBoardRequestDto) {
        Member member = memberService.getCurrentMember();
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);
        ActivityGroupBoard board = ActivityGroupBoard.of(activityGroupBoardRequestDto);
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

        GroupMember groupMember = activityGroupMemberService.getGroupMemberByMemberOrThrow(member);
        if (groupMember.getRole() == ActivityGroupRole.LEADER) {
            List<GroupMember> groupMembers = activityGroupMemberService.getGroupMemberByActivityGroupId(activityGroupId);
            groupMembers
                    .forEach(gMember -> {
                        if (!Objects.equals(gMember.getMember().getId(), member.getId())) {
                            NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                                    .memberId(gMember.getMember().getId())
                                    .content("[" + activityGroup.getName() + "] " + member.getName() + "님이 새 게시글을 등록하였습니다.")
                                    .build();
                            notificationService.createNotification(notificationRequestDto);
                        }
                    });
        } else {
            GroupMember groupLeader = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(activityGroupId, ActivityGroupRole.LEADER);
            NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                    .memberId(groupLeader.getMember().getId())
                    .content("[" + activityGroup.getName() + "] " + member.getName() + "님이 새 게시글을 등록하였습니다.")
                    .build();
            notificationService.createNotification(notificationRequestDto);
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

        if (!memberService.isMemberAdminRole(member) &&
                !memberService.isMemberSuperRole(member) &&
                !activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(activityGroupId, ActivityGroupRole.LEADER).getMember().getId()
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

    public ActivityGroupBoardResponseDto getFeedbackCategoryBoardByParent(Long parentId) {
        ActivityGroupBoard parentBoard = activityGroupBoardRepository.findById(parentId)
                .orElseThrow(() -> new NotFoundException("부모 게시판이 존재하지 않습니다."));

        if (!parentBoard.getCategory().equals(ActivityGroupBoardCategory.SUBMIT)) {
            throw new NotSubmitCategoryBoardException("제출 카테고리 게시판이 아니기 때문에 피드백 카테고리 게시판을 찾을 수 없습니다.");
        }

        if (parentBoard.getChildren().size() != 1) {
            throw new NotFoundException("피드백 카테고리 게시판이 없거나 유일하지 않습니다.");
        }

        Long childId = parentBoard.getChildren().getFirst().getId();
        return getActivityGroupBoardById(childId);
    }

    public ActivityGroupBoardResponseDto getMyAssignmentBoard(Long parentId) {
        Member member = memberService.getCurrentMember();
        ActivityGroupBoard parentBoard = getActivityGroupBoardByIdOrThrow(parentId);
        List<ActivityGroupBoard> childrenBoards = parentBoard.getChildren();
        List<ActivityGroupBoard> assignmentBoard = childrenBoards.stream()
                .filter(child -> child.getCategory() == ActivityGroupBoardCategory.SUBMIT && child.getMember().getId().equals(member.getId()))
                .collect(Collectors.toList());
        if (assignmentBoard.isEmpty()) {
            throw new NotFoundException("제출한 과제가 없습니다.");
        }
        return getActivityGroupBoardById(assignmentBoard.get(0).getId());
    }

    public Long updateActivityGroupBoard(Long activityGroupBoardId, ActivityGroupBoardRequestDto activityGroupBoardRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        ActivityGroupBoard board = getActivityGroupBoardByIdOrThrow(activityGroupBoardId);

        if (!member.getId().equals(board.getMember().getId()) &&
                !memberService.isMemberAdminRole(member) &&
                !memberService.isMemberSuperRole(member)
        ) {
            throw new PermissionDeniedException("활동 그룹 게시판 작성자 또는 운영진만 수정할 수 있습니다.");
        }

        board.setCategory(activityGroupBoardRequestDto.getCategory());
        board.setTitle(activityGroupBoardRequestDto.getTitle());
        board.setContent(activityGroupBoardRequestDto.getContent());
        board.setDueDateTime(activityGroupBoardRequestDto.getDueDateTime());

        List<String> fileUrls = activityGroupBoardRequestDto.getFileUrls();
        if (fileUrls != null) {
            List<UploadedFile> uploadFileList =  fileUrls.stream()
                    .map(fileService::getUploadedFileByUrl)
                    .collect(Collectors.toList());
            board.setUploadedFiles(uploadFileList);
        }

        return activityGroupBoardRepository.save(board).getId();
    }

    public Long deleteActivityGroupBoard(Long activityGroupBoardId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        ActivityGroupBoard board = getActivityGroupBoardByIdOrThrow(activityGroupBoardId);

        if (!member.getId().equals(board.getMember().getId()) &&
                !memberService.isMemberAdminRole(member) &&
                !memberService.isMemberSuperRole(member)
        ) {
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

}