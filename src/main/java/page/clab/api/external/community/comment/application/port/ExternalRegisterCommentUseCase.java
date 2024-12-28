package page.clab.api.external.community.comment.application.port;

import page.clab.api.domain.community.comment.domain.Comment;

public interface ExternalRegisterCommentUseCase {

    Comment save(Comment comment);
}
