package page.clab.api.domain.community.comment.application.port.out;

import java.util.List;
import page.clab.api.domain.community.comment.domain.Comment;

public interface RegisterCommentPort {

    Comment save(Comment comment);

    void saveAll(List<Comment> comments);
}
