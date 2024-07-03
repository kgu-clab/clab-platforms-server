package page.clab.api.domain.comment.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.comment.domain.Comment;

import java.util.Optional;

public interface RetrieveCommentPort {
    Optional<Comment> findById(Long commentId);
    Comment findByIdOrThrow(Long commentId);
    Page<Comment> findAllByIsDeletedTrueAndBoardId(Long boardId, Pageable pageable);
    Page<Comment> findAllByBoardIdAndParentIsNull(Long boardId, Pageable pageable);
    Page<Comment> findAllByWriterId(String memberId, Pageable pageable);
    Long countByBoard(Board board);
}
