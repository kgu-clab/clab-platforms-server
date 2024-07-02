package page.clab.api.domain.comment.application.port.out;

import page.clab.api.domain.comment.domain.Comment;

import java.util.Optional;

public interface LoadCommentPort {
    Optional<Comment> findById(Long commentId);
    Comment findByIdOrThrow(Long commentId);
}
