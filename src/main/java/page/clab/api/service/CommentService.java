package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.CommentRepository;
import page.clab.api.type.dto.CommentRequestDto;
import page.clab.api.type.dto.CommentResponseDto;
import page.clab.api.type.dto.NotificationRequestDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.entity.Board;
import page.clab.api.type.entity.Comment;
import page.clab.api.type.entity.Member;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final BoardService boardService;

    private final MemberService memberService;


    private final NotificationService notificationService;

    public void createComment(Long boardId, CommentRequestDto commentRequestDto) {
        Member member = memberService.getCurrentMember();
        Board board = boardService.getBoardByIdOrThrow(boardId);
        Comment comment = Comment.of(commentRequestDto);
        comment.setBoard(board);
        comment.setWriter(member);
        comment.setCreatedAt(LocalDateTime.now());
        Long id = commentRepository.save(comment).getId();

        NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                .memberId(board.getMember().getId())
                .content("댓글이 등록되었습니다.")
                .build();
        notificationService.createNotification(notificationRequestDto);
        return id;
    }

    public PagedResponseDto<CommentResponseDto> getComments(Long boardId, Pageable pageable) {
        Page<Comment> comments = getCommentByBoardId(boardId, pageable);
        return new PagedResponseDto<>(comments.map(CommentResponseDto::of));
    }

    public PagedResponseDto<CommentResponseDto> getMyComments(Pageable pageable) {
        Member member = memberService.getCurrentMember();
        Page<Comment> comments = getCommentByWriter(member, pageable);
        return new PagedResponseDto<>(comments.map(CommentResponseDto::of));
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

    public Long deleteComment(Long commentId) throws PermissionDeniedException{
        Member member = memberService.getCurrentMember();
        Comment comment = getCommentByIdOrThrow(commentId);
        if (!(comment.getWriter().getId().equals(member.getId()) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException("댓글 작성자만 삭제할 수 있습니다.");
        }
        commentRepository.delete(comment);
        return comment.getId();
    }

    public Comment getCommentByIdOrThrow(Long id){
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
    }

    private Page<Comment> getCommentByBoardId(Long boardId, Pageable pageable) {
        return commentRepository.findAllByBoardIdOrderByCreatedAtDesc(boardId, pageable);
    }

    private Page<Comment> getCommentByWriter(Member member, Pageable pageable) {
        return commentRepository.findAllByWriterOrderByCreatedAtDesc(member, pageable);
    }

}
