package page.clab.api.domain.comment.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.comment.domain.Comment;

public interface RetrieveDeletedCommentsPort {
    Page<Comment> findAllByIsDeletedTrueAndBoardId(Long boardId, Pageable pageable);
}
