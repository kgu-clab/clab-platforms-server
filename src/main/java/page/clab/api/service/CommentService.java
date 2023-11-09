package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.CommentRepository;
import page.clab.api.type.dto.CommentDto;
import page.clab.api.type.entity.Board;
import page.clab.api.type.entity.Comment;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final BoardService boardService;

    private final MemberService memberService;

    public void createComment(Long boardId, CommentDto commentDto) {
        Board board = boardService.getBoardById(boardId);
        Comment comment = Comment.of(commentDto);
        comment.setWriter(memberService.getCurrentMember());
        comment.setBoard(board);
        commentRepository.save(comment);
    }

    public void updateComment(Long commentId, CommentDto commentDto) throws PermissionDeniedException {
        Comment comment = getCommentByIdOrThrow(commentId);
        if (!Objects.equals(commentDto.getWriter_id(), comment.getWriter().getId())){
            throw new PermissionDeniedException("댓글 작성자만 수정할 수 있습니다.");
        }
        Comment updateComment = Comment.of(commentDto);
        updateComment.setId(comment.getId());
        updateComment.setBoard(comment.getBoard());
        updateComment.setWriter(comment.getWriter());
        updateComment.setUpdateTime(commentDto.getUpdateTime());
        updateComment.setContent(commentDto.getContent());
        commentRepository.save(updateComment);
    }

    public void deleteComment(Long commentId) throws PermissionDeniedException{
        Comment comment = getCommentByIdOrThrow(commentId);
        if (!Objects.equals(comment.getWriter().getId(), memberService.getCurrentMember().getId())) {
            throw new PermissionDeniedException("댓글 작성자만 삭제할 수 있습니다.");
        }
        commentRepository.delete(comment);
    }

    public List<CommentDto> getComments(Long boardId) {
        List<Comment> comments = commentRepository.findAllByBoardId(boardId);
        return comments.stream()
                .map(CommentDto::of)
                .collect(Collectors.toList());
    }

    public Comment getCommentByIdOrThrow(Long id){
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
    }

}
