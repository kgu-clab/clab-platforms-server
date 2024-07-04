package page.clab.api.domain.board.application.port.in;

import page.clab.api.domain.board.dto.request.BoardUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface UpdateBoardUseCase {
    String updateBoard(Long boardId, BoardUpdateRequestDto requestDto) throws PermissionDeniedException;
}
