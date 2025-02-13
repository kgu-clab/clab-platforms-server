package page.clab.api.domain.community.comment.application.port.in;

import page.clab.api.domain.community.comment.application.dto.request.CommentUpdateRequestDto;

public interface UpdateCommentUseCase {

    Long updateComment(Long commentId, CommentUpdateRequestDto requestDto);
}
