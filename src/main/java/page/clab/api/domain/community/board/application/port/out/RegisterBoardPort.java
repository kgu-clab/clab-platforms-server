package page.clab.api.domain.community.board.application.port.out;

import page.clab.api.domain.community.board.domain.Board;

import java.util.List;

public interface RegisterBoardPort {
    Board save(Board board);

    void saveAll(List<Board> boards);
}
