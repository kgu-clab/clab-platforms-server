package page.clab.api.domain.board.application;

import page.clab.api.domain.board.dto.request.BoardRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface BoardRegisterService {
    String register(BoardRequestDto requestDto) throws PermissionDeniedException;
}
