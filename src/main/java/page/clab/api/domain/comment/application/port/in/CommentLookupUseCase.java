package page.clab.api.domain.comment.application.port.in;

import page.clab.api.domain.comment.domain.Comment;

public interface CommentLookupUseCase {
    Comment getCommentByIdOrThrow(Long commentId);
}
