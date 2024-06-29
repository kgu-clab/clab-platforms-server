package page.clab.api.domain.comment.application;

import page.clab.api.domain.comment.domain.Comment;

public interface CommentLookupService {
    Comment getCommentByIdOrThrow(Long commentId);
}
