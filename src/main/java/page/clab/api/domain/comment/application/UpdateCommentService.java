package page.clab.api.domain.comment.application;

import page.clab.api.domain.comment.dto.request.CommentUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface UpdateCommentService {
    Long execute(Long commentId, CommentUpdateRequestDto requestDto) throws PermissionDeniedException;
}