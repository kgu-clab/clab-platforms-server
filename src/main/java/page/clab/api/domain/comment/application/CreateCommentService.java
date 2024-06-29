package page.clab.api.domain.comment.application;

import page.clab.api.domain.comment.dto.request.CommentRequestDto;

public interface CreateCommentService {
    Long execute(Long parentId, Long boardId, CommentRequestDto requestDto);
}