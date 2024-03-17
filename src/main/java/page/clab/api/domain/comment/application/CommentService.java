package page.clab.api.domain.comment.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;

    private final CommentLikeRepository commentLikeRepository;

    private final BoardService boardService;

    private final MemberService memberService;

    private final NotificationService notificationService;

    @Transactional
    public Long createComment(Long parentId, Long boardId, CommentRequestDto commentRequestDto) {
        Member currentMember = memberService.getCurrentMember();
        Board board = boardService.getBoardByIdOrThrow(boardId);
        Comment parent = parentId != null ? getCommentByIdOrThrow(parentId) : null;
        Comment comment = Comment.of(commentRequestDto, board, currentMember, parent);
        if (parent != null) {
            parent.addChildComment(comment);
        }
        notificationService.sendNotificationToMember(
                board.getMember(),
                "[" + board.getTitle() + "] " + comment.getWriter() + "님이 게시글에 댓글을 남겼습니다."
        );
        return commentRepository.save(comment).getId();
    }

    public PagedResponseDto<CommentGetAllResponseDto> getComments(Long boardId, Pageable pageable) {
        Member member = memberService.getCurrentMember();
        Page<Comment> comments = getCommentByBoardIdAndParentIsNull(boardId, pageable);
        comments.forEach(comment -> Hibernate.initialize(comment.getChildren()));
        Page<CommentGetAllResponseDto> pagedResponseDto = comments.map(dto -> CommentGetAllResponseDto.of(dto, member.getId()));
        pagedResponseDto.forEach(dto -> setHasLikeByMeAtCommentGetAllResponseDto(dto, member));
        return new PagedResponseDto<>(pagedResponseDto);
    }

    public PagedResponseDto<CommentGetMyResponseDto> getMyComments(Pageable pageable) {
        Member member = memberService.getCurrentMember();
        Page<Comment> comments = getCommentByWriter(member, pageable);
        Page<CommentGetMyResponseDto> pagedResponseDto = comments.map(CommentGetMyResponseDto::of);
        pagedResponseDto.forEach(dto -> setHasLikeByMeAtCommentGetMyResponseDto(dto, member));
        return new PagedResponseDto<>(comments.map(CommentGetMyResponseDto::of));
    }

    public Long updateComment(Long commentId, CommentUpdateRequestDto commentUpdateRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Comment comment = getCommentByIdOrThrow(commentId);
        validateCommentUpdatePermission(comment, member);
        comment.update(commentUpdateRequestDto);
        return commentRepository.save(comment).getId();
    }

    public Long deleteComment(Long commentId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Comment comment = getCommentByIdOrThrow(commentId);
        validateCommentUpdatePermission(comment, member);
        commentRepository.delete(comment);
        return comment.getId();
    }

    @Transactional
    public Long updateLikes(Long commentId) {
        Member member = memberService.getCurrentMember();
        Comment comment = getCommentByIdOrThrow(commentId);
        CommentLike commentLike = commentLikeRepository.findByCommentIdAndMemberId(comment.getId(), member.getId());

        if (commentLike != null) {
            comment.removeLike();
            commentLikeRepository.delete(commentLike);
        }
        else {
            comment.addLike();
            CommentLike newCommentLike= CommentLike.builder()
                    .memberId(member.getId())
                    .commentId(comment.getId())
                    .build();
            commentLikeRepository.save(newCommentLike);
        }

        return comment.getLikes();
    }

    public void setHasLikeByMeAtCommentGetAllResponseDto(CommentGetAllResponseDto commentGetAllResponseDto, Member member) {
        Comment comment = getCommentByIdOrThrow(commentGetAllResponseDto.getId());
        commentGetAllResponseDto.setHasLikeByMe(commentLikeRepository.existsByCommentIdAndMemberId(comment.getId(), member.getId()));
        commentGetAllResponseDto.getChildren().forEach(dto -> setHasLikeByMeAtCommentGetAllResponseDto(dto, member));
    }

    public void setHasLikeByMeAtCommentGetMyResponseDto(CommentGetMyResponseDto commentGetMyResponseDto, Member member) {
        Comment comment = getCommentByIdOrThrow(commentGetMyResponseDto.getId());
        commentGetMyResponseDto.setHasLikeByMe(commentLikeRepository.existsByCommentIdAndMemberId(comment.getId(), member.getId()));
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

    private void validateCommentUpdatePermission(Comment comment, Member member) throws PermissionDeniedException {
        if (!(comment.isOwnedBy(member) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException("댓글 작성자 또는 관리자만 수정할 수 있습니다.");
        }
    }

}