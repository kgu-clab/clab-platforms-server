package page.clab.api.external.community.comment.application.port;

import page.clab.api.domain.community.comment.domain.Comment;

public interface ExternalRetrieveCommentUseCase {

    Comment findByIdOrThrow(Long targetId);

    Long countByBoardId(Long id);

    long countCommentsByBoardId(Long id);
}
