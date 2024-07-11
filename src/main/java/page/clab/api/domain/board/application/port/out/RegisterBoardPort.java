package page.clab.api.domain.board.application.port.out;

import page.clab.api.domain.board.domain.Board;

import java.util.List;

public interface RegisterBoardPort {
    Board save(Board board);

    void saveAll(List<Board> boards);
}
