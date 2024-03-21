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
import page.clab.api.domain.comment.dto.response.CommentGetAllResponseDto;
import page.clab.api.domain.comment.dto.response.CommentGetMyResponseDto;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final BoardService boardService;

    private final MemberService memberService;

    private final NotificationService notificationService;

    private final ValidationService validationService;

    private final CommentRepository commentRepository;

    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public Long createComment(Long parentId, Long boardId, CommentRequestDto dto) {
        Comment comment = createAndStoreComment(parentId, boardId, dto);
        sendNotificationForNewComment(comment);
        return comment.getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<CommentGetAllResponseDto> getAllComments(Long boardId, Pageable pageable) {
        Member currentMember = memberService.getCurrentMember();
        Page<Comment> comments = getCommentByBoardIdAndParentIsNull(boardId, pageable);
        comments.forEach(comment -> Hibernate.initialize(comment.getChildren()));
        Page<CommentGetAllResponseDto> commentDtos = comments.map(comment -> toCommentGetAllResponseDto(comment, currentMember));
        return new PagedResponseDto<>(commentDtos);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<CommentGetMyResponseDto> getMyComments(Pageable pageable) {
        Member currentMember = memberService.getCurrentMember();
        Page<Comment> comments = getCommentByWriter(currentMember, pageable);
        Page<CommentGetMyResponseDto> commentDtos = comments.map(comment -> toCommentGetMyResponseDto(comment, currentMember));
        return new PagedResponseDto<>(commentDtos);
    }

    public Long updateComment(Long commentId, CommentUpdateRequestDto dto) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        Comment comment = getCommentByIdOrThrow(commentId);
        comment.validateAccessPermission(currentMember);
        comment.update(dto);
        validationService.checkValid(comment);
        return commentRepository.save(comment).getId();
    }

    public Long deleteComment(Long commentId) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        Comment comment = getCommentByIdOrThrow(commentId);
        comment.validateAccessPermission(currentMember);
        commentRepository.delete(comment);
        return comment.getId();
    }

    @Transactional
    public Long toggleLikeStatus(Long commentId) {
        Member currentMember = memberService.getCurrentMember();
        Comment comment = getCommentByIdOrThrow(commentId);
        Optional<CommentLike> commentLikeOpt = commentLikeRepository.findByCommentIdAndMemberId(comment.getId(), currentMember.getId());
        if (commentLikeOpt.isPresent()) {
            comment.decrementLikes();
            commentLikeRepository.delete(commentLikeOpt.get());
        } else {
            comment.incrementLikes();
            CommentLike newLike = new CommentLike(currentMember.getId(), comment.getId());
            commentLikeRepository.save(newLike);
        }
        return comment.getLikes();
    }

    public boolean isCommentExistById(Long id) {
        return commentRepository.existsById(id);
    }

    public Comment getCommentByIdOrThrow(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
    }

    private Page<Comment> getCommentByBoardIdAndParentIsNull(Long boardId, Pageable pageable) {
        return commentRepository.findAllByBoardIdAndParentIsNullOrderByCreatedAtDesc(boardId, pageable);
    }

    private Page<Comment> getCommentByWriter(Member member, Pageable pageable) {
        return commentRepository.findAllByWriterOrderByCreatedAtDesc(member, pageable);
    }

    private Comment createAndStoreComment(Long parentId, Long boardId, CommentRequestDto dto) {
        Member currentMember = memberService.getCurrentMember();
        Board board = boardService.getBoardByIdOrThrow(boardId);
        Comment parent = findParentComment(parentId);
        Comment comment = Comment.create(dto, board, currentMember, parent);
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

    private CommentGetAllResponseDto toCommentGetAllResponseDto(Comment comment, Member currentMember) {
        CommentGetAllResponseDto dto = CommentGetAllResponseDto.of(comment, currentMember.getId());
        dto.setHasLikeByMe(checkLikeStatus(comment.getId(), currentMember.getId()));
        dto.getChildren().forEach(childDto -> setLikeStatusForChildren(childDto, currentMember));
        return dto;
    }

    private boolean checkLikeStatus(Long commentId, String memberId) {
        return commentLikeRepository.existsByCommentIdAndMemberId(commentId, memberId);
    }

    private void setLikeStatusForChildren(CommentGetAllResponseDto dto, Member member) {
        dto.setHasLikeByMe(checkLikeStatus(dto.getId(), member.getId()));
        dto.getChildren().forEach(childDto -> setLikeStatusForChildren(childDto, member));
    }

    private CommentGetMyResponseDto toCommentGetMyResponseDto(Comment comment, Member currentMember) {
        CommentGetMyResponseDto dto = CommentGetMyResponseDto.of(comment);
        dto.setHasLikeByMe(checkLikeStatus(comment.getId(), currentMember.getId()));
        return dto;
    }

}