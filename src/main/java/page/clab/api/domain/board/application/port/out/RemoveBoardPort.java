package page.clab.api.domain.board.application.port.out;

import page.clab.api.domain.board.domain.Board;

public interface RemoveBoardPort {
    void delete(Board board);
}
