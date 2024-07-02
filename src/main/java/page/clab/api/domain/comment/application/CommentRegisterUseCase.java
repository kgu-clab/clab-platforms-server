package page.clab.api.domain.comment.application;

import page.clab.api.domain.comment.dto.request.CommentRequestDto;

public interface CommentRegisterUseCase {
    Long register(Long parentId, Long boardId, CommentRequestDto requestDto);
}