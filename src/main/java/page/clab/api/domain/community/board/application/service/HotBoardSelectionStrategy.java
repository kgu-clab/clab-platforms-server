package page.clab.api.domain.community.board.application.service;

import page.clab.api.domain.community.board.domain.Board;

import java.util.List;

public interface HotBoardSelectionStrategy {
    List<Board> getHotBoards();
}
