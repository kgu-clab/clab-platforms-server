package page.clab.api.domain.comment.application.port.out;

import page.clab.api.domain.comment.domain.Comment;

import java.util.List;

public interface RegisterCommentPort {
    Comment save(Comment comment);

    void saveAll(List<Comment> comments);
}
