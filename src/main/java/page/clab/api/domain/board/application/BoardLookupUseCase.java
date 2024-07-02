package page.clab.api.domain.board.application;

import page.clab.api.domain.board.domain.Board;

public interface BoardLookupUseCase {
    Board getBoardByIdOrThrow(Long boardId);
}
