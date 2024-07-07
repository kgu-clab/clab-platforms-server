package page.clab.api.domain.board.application.port.in;

import page.clab.api.domain.board.dto.request.BoardRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface RegisterBoardUseCase {
    String registerBoard(BoardRequestDto requestDto) throws PermissionDeniedException;
}
