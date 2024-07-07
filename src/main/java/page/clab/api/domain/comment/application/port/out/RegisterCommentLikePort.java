package page.clab.api.domain.comment.application.port.out;

import page.clab.api.domain.comment.domain.CommentLike;

public interface RegisterCommentLikePort {
    CommentLike save(CommentLike commentLike);
}
