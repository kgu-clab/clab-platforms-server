package page.clab.api.domain.comment.application.port.out;

import page.clab.api.domain.comment.domain.Comment;

public interface RemoveCommentPort {
    void delete(Comment comment);
}
