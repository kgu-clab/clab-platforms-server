package page.clab.api.domain.community.comment.application.port.in;

import page.clab.api.domain.community.comment.application.dto.request.CommentUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface UpdateCommentUseCase {

    Long updateComment(Long commentId, CommentUpdateRequestDto requestDto) throws PermissionDeniedException;
}
