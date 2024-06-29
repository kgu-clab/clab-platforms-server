package page.clab.api.domain.board.application;

import page.clab.api.domain.board.domain.Board;

public interface BoardLookupService {
    Board getBoardByIdOrThrow(Long boardId);
}
