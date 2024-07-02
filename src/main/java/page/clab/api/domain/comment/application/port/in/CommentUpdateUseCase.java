package page.clab.api.domain.comment.application.port.in;

import page.clab.api.domain.comment.dto.request.CommentUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface CommentUpdateUseCase {
    Long update(Long commentId, CommentUpdateRequestDto requestDto) throws PermissionDeniedException;
}