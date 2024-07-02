package page.clab.api.domain.board.application.port.out;

import page.clab.api.domain.board.domain.Board;

import java.util.Optional;

public interface LoadBoardPort {
    Optional<Board> findById(Long boardId);
    Board findByIdOrThrow(Long boardId);
}
