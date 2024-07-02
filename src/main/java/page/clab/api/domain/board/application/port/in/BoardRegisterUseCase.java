package page.clab.api.domain.board.application.port.in;

import page.clab.api.domain.board.dto.request.BoardRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface BoardRegisterUseCase {
    String register(BoardRequestDto requestDto) throws PermissionDeniedException;
}
