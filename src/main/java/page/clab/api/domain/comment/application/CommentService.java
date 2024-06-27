package page.clab.api.domain.comment.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.BoardService;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.comment.dao.CommentRepository;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.comment.dto.request.CommentRequestDto;
import page.clab.api.domain.comment.dto.request.CommentUpdateRequestDto;
import page.clab.api.domain.comment.dto.response.CommentMyResponseDto;
import page.clab.api.domain.comment.dto.response.CommentResponseDto;
import page.clab.api.domain.comment.dto.response.DeletedCommentResponseDto;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final BoardService boardService;

    private final MemberLookupService memberLookupService;

    private final NotificationService notificationService;

    private final ValidationService validationService;

    private final CommentRepository commentRepository;

    @Transactional
    public Long createComment(Long parentId, Long boardId, CommentRequestDto requestDto) {
        Comment comment = createAndStoreComment(parentId, boardId, requestDto);
        sendNotificationForNewComment(comment);
        return boardId;
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<CommentResponseDto> getAllComments(Long boardId, Pageable pageable) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        Page<Comment> comments = getCommentByBoardIdAndParentIsNull(boardId, pageable);
        List<CommentResponseDto> commentDtos = comments.stream()
                .map(comment -> toCommentResponseDtoWithMemberInfo(comment, currentMemberId))
                .toList();
        return new PagedResponseDto<>(new PageImpl<>(commentDtos, pageable, comments.getTotalElements()));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<CommentMyResponseDto> getMyComments(Pageable pageable) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        Page<Comment> comments = getCommentByWriterId(currentMemberId, pageable);
        List<CommentMyResponseDto> dtos = comments.stream()
                .map(comment -> toCommentMyResponseDto(comment, currentMemberId))
                .filter(Objects::nonNull)
                .toList();
        return new PagedResponseDto<>(new PageImpl<>(dtos, pageable, comments.getTotalElements()));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<DeletedCommentResponseDto> getDeletedComments(Long boardId, Pageable pageable) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        Page<Comment> comments = commentRepository.findAllByIsDeletedTrueAndBoardId(boardId, pageable);
        List<DeletedCommentResponseDto> deletedCommentDtos = comments.stream()
                .map(comment -> {
                    MemberDetailedInfoDto memberInfo = memberLookupService.getMemberDetailedInfoById(comment.getWriterId());
                    return DeletedCommentResponseDto.toDto(comment, memberInfo, comment.isOwner(currentMemberId));
                })
                .toList();
        return new PagedResponseDto<>(new PageImpl<>(deletedCommentDtos, pageable, comments.getTotalElements()));
    }

    @Transactional
    public Long updateComment(Long commentId, CommentUpdateRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto memberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        Comment comment = getCommentByIdOrThrow(commentId);
        comment.validateAccessPermission(memberInfo);
        comment.update(requestDto);
        validationService.checkValid(comment);
        commentRepository.save(comment);
        return comment.getBoard().getId();
    }

    public Long deleteComment(Long commentId) throws PermissionDeniedException {
        MemberDetailedInfoDto memberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        Comment comment = getCommentByIdOrThrow(commentId);
        comment.validateAccessPermission(memberInfo);
        comment.delete();
        commentRepository.save(comment);
        return comment.getBoard().getId();
    }

    public Comment getCommentByIdOrThrow(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
    }

    private Page<Comment> getCommentByBoardIdAndParentIsNull(Long boardId, Pageable pageable) {
        return commentRepository.findAllByBoardIdAndParentIsNull(boardId, pageable);
    }

    private Page<Comment> getCommentByWriterId(String memberId, Pageable pageable) {
        return commentRepository.findAllByWriterId(memberId, pageable);
    }

    private Comment createAndStoreComment(Long parentId, Long boardId, CommentRequestDto requestDto) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        Board board = boardService.getBoardByIdOrThrow(boardId);
        Comment parent = findParentComment(parentId);
        Comment comment = CommentRequestDto.toEntity(requestDto, board, currentMemberId, parent);
        if (parent != null) {
            parent.addChildComment(comment);
        }
        validationService.checkValid(comment);
        return commentRepository.save(comment);
    }

    private Comment findParentComment(Long parentId) {
        return parentId != null ? getCommentByIdOrThrow(parentId) : null;
    }

    private void sendNotificationForNewComment(Comment comment) {
        Board board = comment.getBoard();
        MemberBasicInfoDto memberInfo = memberLookupService.getMemberBasicInfoById(comment.getWriterId());
        String notificationMessage = String.format("[%s] %s님이 게시글에 댓글을 남겼습니다.", board.getTitle(), memberInfo.getMemberName());
        notificationService.sendNotificationToMember(board.getMemberId(), notificationMessage);
    }

    private CommentResponseDto toCommentResponseDtoWithMemberInfo(Comment comment, String currentMemberId) {
        MemberDetailedInfoDto memberInfo = memberLookupService.getMemberDetailedInfoById(comment.getWriterId());
        List<CommentResponseDto> childrenDtos = comment.getChildren().stream()
                .map(child -> toCommentResponseDtoWithMemberInfo(child, currentMemberId))
                .toList();
        boolean isOwner = comment.isOwner(currentMemberId);
        return CommentResponseDto.toDto(comment, memberInfo, isOwner, childrenDtos);
    }

    private CommentMyResponseDto toCommentMyResponseDto(Comment comment, String currentMemberId) {
        MemberDetailedInfoDto memberInfo = memberLookupService.getMemberDetailedInfoById(comment.getWriterId());
        return CommentMyResponseDto.toDto(comment, memberInfo);
    }

}