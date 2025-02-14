package page.clab.api.domain.community.comment.application.port.out;

import page.clab.api.domain.community.comment.domain.CommentLike;

public interface RegisterCommentLikePort {

    CommentLike save(CommentLike commentLike);
}
