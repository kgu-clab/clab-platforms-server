package page.clab.api.domain.board.application;

import page.clab.api.domain.board.dto.request.BoardUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface BoardUpdateUseCase {
    String update(Long boardId, BoardUpdateRequestDto requestDto) throws PermissionDeniedException;
}
