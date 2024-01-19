package page.clab.api.domain.comment.application;

import java.time.LocalDateTime;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.board.application.BoardService;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.domain.comment.dao.CommentRepository;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.domain.comment.dto.response.CommentGetAllResponseDto;
import page.clab.api.domain.comment.dto.response.CommentGetMyResponseDto;
import page.clab.api.domain.comment.dto.request.CommentRequestDto;
import page.clab.api.domain.notification.dto.request.NotificationRequestDto;
import page.clab.api.global.dto.PagedResponseDto;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.member.domain.Member;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;

    private final BoardService boardService;

    private final MemberService memberService;

    private final NotificationService notificationService;

    @Transactional
    public Long createComment(Long parentId, Long boardId, CommentRequestDto commentRequestDto) {
        Member member = memberService.getCurrentMember();
        Board board = boardService.getBoardByIdOrThrow(boardId);
        Comment comment = Comment.of(commentRequestDto);
        comment.setBoard(board);
        comment.setWriter(member);
        comment.setCreatedAt(LocalDateTime.now());
        if (parentId != null) {
            Comment parentComment = getCommentByIdOrThrow(parentId);
            comment.setParent(parentComment);
            parentComment.getChildren().add(comment);
            commentRepository.save(parentComment);
        }
        Long id = commentRepository.save(comment).getId();

        NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                .memberId(board.getMember().getId())
                .content("[" + board.getTitle() + "] " + member.getName() + "님이 게시글에 댓글을 남겼습니다.")
                .build();
        notificationService.createNotification(notificationRequestDto);
        return id;
    }

    public PagedResponseDto<CommentGetAllResponseDto> getComments(Long boardId, Pageable pageable) {
        Page<Comment> comments = getCommentByBoardIdAndParentIsNull(boardId, pageable);
        comments.forEach(comment -> Hibernate.initialize(comment.getChildren()));
        return new PagedResponseDto<>(comments.map(CommentGetAllResponseDto::of));
    }

    public PagedResponseDto<CommentGetMyResponseDto> getMyComments(Pageable pageable) {
        Member member = memberService.getCurrentMember();
        Page<Comment> comments = getCommentByWriter(member, pageable);
        return new PagedResponseDto<>(comments.map(CommentGetMyResponseDto::of));
    }

    public Long updateComment(Long commentId, CommentRequestDto commentRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Comment comment = getCommentByIdOrThrow(commentId);
        if (!(comment.getWriter().getId().equals(member.getId()) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException("댓글 작성자만 수정할 수 있습니다.");
        }
        comment.setContent(commentRequestDto.getContent());
        comment.setUpdateTime(LocalDateTime.now());
        return commentRepository.save(comment).getId();
    }

    public Long deleteComment(Long commentId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Comment comment = getCommentByIdOrThrow(commentId);
        if (!(comment.getWriter().getId().equals(member.getId()) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException("댓글 작성자만 삭제할 수 있습니다.");
        }
        commentRepository.delete(comment);
        return comment.getId();
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

}