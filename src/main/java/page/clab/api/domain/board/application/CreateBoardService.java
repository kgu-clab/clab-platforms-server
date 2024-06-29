package page.clab.api.domain.board.application;

import page.clab.api.domain.board.dto.request.BoardRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface CreateBoardService {
    String createBoard(BoardRequestDto requestDto) throws PermissionDeniedException;
}
