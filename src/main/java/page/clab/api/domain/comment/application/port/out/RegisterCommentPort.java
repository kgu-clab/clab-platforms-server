package page.clab.api.domain.comment.application.port.out;

import page.clab.api.domain.comment.domain.Comment;

public interface RegisterCommentPort {
    Comment save(Comment comment);
}
