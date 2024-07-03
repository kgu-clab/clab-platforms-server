package page.clab.api.domain.comment.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.comment.application.port.out.RegisterCommentPort;
import page.clab.api.domain.comment.application.port.out.RemoveCommentPort;
import page.clab.api.domain.comment.application.port.out.RetrieveCommentPort;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentPersistenceAdapter implements
        RegisterCommentPort,
        RemoveCommentPort,
        RetrieveCommentPort {

    private final CommentRepository commentRepository;

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    @Override
    public Optional<Comment> findById(Long commentId) {
        return commentRepository.findById(commentId);
    }

    @Override
    public Comment findByIdOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("[Comment] id: " + commentId + "에 해당하는 댓글이 존재하지 않습니다."));
    }

    @Override
    public Page<Comment> findAllByIsDeletedTrueAndBoardId(Long boardId, Pageable pageable) {
        return commentRepository.findAllByIsDeletedTrueAndBoardId(boardId, pageable);
    }

    @Override
    public Page<Comment> findAllByBoardIdAndParentIsNull(Long boardId, Pageable pageable) {
        return commentRepository.findAllByBoardIdAndParentIsNull(boardId, pageable);
    }

    @Override
    public Page<Comment> findAllByWriterId(String memberId, Pageable pageable) {
        return commentRepository.findAllByWriterId(memberId, pageable);
    }

    @Override
    public Long countByBoard(Board board) {
        return commentRepository.countByBoard(board);
    }
}
