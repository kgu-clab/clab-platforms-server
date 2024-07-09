package page.clab.api.domain.comment.application.port.out;

import page.clab.api.domain.comment.domain.CommentLike;

public interface RemoveCommentLikePort {
    void delete(CommentLike commentLike);
}
