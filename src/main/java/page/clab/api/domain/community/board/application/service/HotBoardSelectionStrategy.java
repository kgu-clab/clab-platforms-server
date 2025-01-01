package page.clab.api.domain.community.board.application.service;

import java.util.List;
import page.clab.api.domain.community.board.domain.Board;

public interface HotBoardSelectionStrategy {
    List<Board> getHotBoards();
}
