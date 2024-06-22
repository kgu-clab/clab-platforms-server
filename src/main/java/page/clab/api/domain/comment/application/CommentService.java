package page.clab.api.domain.comment.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.BoardService;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.comment.dao.CommentLikeRepository;
import page.clab.api.domain.comment.dao.CommentRepository;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.comment.domain.CommentLike;
import page.clab.api.domain.comment.dto.request.CommentRequestDto;
import page.clab.api.domain.comment.dto.request.CommentUpdateRequestDto;
import page.clab.api.domain.comment.dto.response.CommentMyResponseDto;
import page.clab.api.domain.comment.dto.response.CommentResponseDto;
import page.clab.api.domain.comment.dto.response.DeletedCommentResponseDto;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

import java.util.Comparator;
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

    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public Long createComment(Long parentId, Long boardId, CommentRequestDto requestDto) {
        Comment comment = createAndStoreComment(parentId, boardId, requestDto);
        sendNotificationForNewComment(comment);
        return boardId;
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<CommentResponseDto> getAllComments(Long boardId, Pageable pageable) {
        Member currentMember = memberLookupService.getCurrentMember();
        Page<Comment> comments = getCommentByBoardIdAndParentIsNull(boardId, pageable);
        comments.forEach(comment -> {
            Hibernate.initialize(comment.getChildren());
            sortChildrenComments(comment);
        });
        Page<CommentResponseDto> commentDtos = comments.map(comment -> toCommentResponseDto(comment, currentMember));
        return new PagedResponseDto<>(commentDtos);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<CommentMyResponseDto> getMyComments(Pageable pageable) {
        Member currentMember = memberLookupService.getCurrentMember();
        Page<Comment> comments = getCommentByWriter(currentMember, pageable);
        List<CommentMyResponseDto> dtos = comments
                .map(comment -> toCommentMyResponseDto(comment, currentMember))
                .stream()
                .filter(Objects::nonNull)
                .toList();
        return new PagedResponseDto<>(dtos, pageable, dtos.size());
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<DeletedCommentResponseDto> getDeletedComments(Long boardId, Pageable pageable) {
        Member currentMember = memberLookupService.getCurrentMember();
        Page<Comment> comments = commentRepository.findAllByIsDeletedTrueAndBoardId(boardId, pageable);
        return new PagedResponseDto<>(comments.map(comment -> DeletedCommentResponseDto.toDto(comment, currentMember.getId())));
    }

    @Transactional
    public Long updateComment(Long commentId, CommentUpdateRequestDto requestDto) throws PermissionDeniedException {
        Member currentMember = memberLookupService.getCurrentMember();
        Comment comment = getCommentByIdOrThrow(commentId);
        comment.validateAccessPermission(currentMember);
        comment.update(requestDto);
        validationService.checkValid(comment);
        commentRepository.save(comment);
        return comment.getBoard().getId();
    }

    public Long deleteComment(Long commentId) throws PermissionDeniedException {
        Member currentMember = memberLookupService.getCurrentMember();
        Comment comment = getCommentByIdOrThrow(commentId);
        comment.validateAccessPermission(currentMember);
        comment.updateIsDeleted();
        commentRepository.save(comment);
        return comment.getBoard().getId();
    }

    @Transactional
    public Long toggleLikeStatus(Long commentId) {
        Member currentMember = memberLookupService.getCurrentMember();
        Comment comment = getCommentByIdOrThrow(commentId);
        Optional<CommentLike> commentLikeOpt = commentLikeRepository.findByCommentIdAndMemberId(comment.getId(), currentMember.getId());
        if (commentLikeOpt.isPresent()) {
            comment.decrementLikes();
            commentLikeRepository.delete(commentLikeOpt.get());
        } else {
            comment.incrementLikes();
            CommentLike newLike = CommentLike.create(currentMember.getId(), comment.getId());
            commentLikeRepository.save(newLike);
        }
        return comment.getLikes();
    }

    public Comment getCommentByIdOrThrow(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
    }

    private Page<Comment> getCommentByBoardIdAndParentIsNull(Long boardId, Pageable pageable) {
        return commentRepository.findAllByBoardIdAndParentIsNull(boardId, pageable);
    }

    private Page<Comment> getCommentByWriter(Member member, Pageable pageable) {
        return commentRepository.findAllByWriter(member, pageable);
    }

    private Comment createAndStoreComment(Long parentId, Long boardId, CommentRequestDto requestDto) {
        Member currentMember = memberLookupService.getCurrentMember();
        Board board = boardService.getBoardByIdOrThrow(boardId);
        Comment parent = findParentComment(parentId);
        Comment comment = CommentRequestDto.toEntity(requestDto, board, currentMember, parent);
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
        Member boardOwner = board.getMember();
        String notificationMessage = String.format("[%s] %s님이 게시글에 댓글을 남겼습니다.", board.getTitle(), comment.getWriterName());
        notificationService.sendNotificationToMember(boardOwner, notificationMessage);
    }

    private CommentResponseDto toCommentResponseDto(Comment comment, Member currentMember) {
        Boolean hasLikeByMe = checkLikeStatus(comment.getId(), currentMember.getId());
        CommentResponseDto responseDto = CommentResponseDto.toDto(comment, currentMember.getId());
        responseDto.getChildren().forEach(childDto -> setLikeStatusForChildren(childDto, currentMember));
        if (!responseDto.getIsDeleted()) {
            responseDto.setHasLikeByMe(hasLikeByMe);
        }
        return responseDto;
    }

    private boolean checkLikeStatus(Long commentId, String memberId) {
        return commentLikeRepository.existsByCommentIdAndMemberId(commentId, memberId);
    }

    private void setLikeStatusForChildren(CommentResponseDto responseDto, Member member) {
        responseDto.setHasLikeByMe(checkLikeStatus(responseDto.getId(), member.getId()));
        responseDto.getChildren().forEach(childDto -> setLikeStatusForChildren(childDto, member));
    }

    private CommentMyResponseDto toCommentMyResponseDto(Comment comment, Member currentMember) {
        boolean hasLikeByMe = checkLikeStatus(comment.getId(), currentMember.getId());
        return CommentMyResponseDto.toDto(comment, hasLikeByMe);
    }

    private void sortChildrenComments(Comment comment) {
        comment.getChildren().sort(Comparator.comparing(Comment::getCreatedAt));
    }

}