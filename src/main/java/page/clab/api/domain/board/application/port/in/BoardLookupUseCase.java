package page.clab.api.domain.board.application.port.in;

import page.clab.api.domain.board.domain.Board;

public interface BoardLookupUseCase {
    Board getBoardByIdOrThrow(Long boardId);
}
