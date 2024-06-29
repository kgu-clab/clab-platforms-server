package page.clab.api.domain.comment.application;

import page.clab.api.global.exception.PermissionDeniedException;

public interface DeleteCommentService {
    Long execute(Long commentId) throws PermissionDeniedException;
}