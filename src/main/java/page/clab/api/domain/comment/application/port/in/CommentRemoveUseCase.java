package page.clab.api.domain.comment.application.port.in;

import page.clab.api.global.exception.PermissionDeniedException;

public interface CommentRemoveUseCase {
    Long remove(Long commentId) throws PermissionDeniedException;
}