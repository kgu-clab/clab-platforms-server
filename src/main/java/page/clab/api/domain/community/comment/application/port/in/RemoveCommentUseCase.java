package page.clab.api.domain.community.comment.application.port.in;

import page.clab.api.global.exception.PermissionDeniedException;

public interface RemoveCommentUseCase {

    Long removeComment(Long commentId) throws PermissionDeniedException;
}
