package page.clab.api.domain.community.board.application.port.out;

import page.clab.api.domain.community.board.domain.Board;

public interface RegisterBoardPort {
    Board save(Board board);
}
