package page.clab.api.domain.community.comment.application.port.in;

import page.clab.api.domain.community.comment.application.dto.request.CommentRequestDto;

public interface RegisterCommentUseCase {

    Long registerComment(Long parentId, Long boardId, CommentRequestDto requestDto);
}
