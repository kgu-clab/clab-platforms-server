package page.clab.api.domain.comment.application.port.out;

import page.clab.api.domain.board.domain.Board;

public interface CountCommentsByBoardPort {
    Long countByBoard(Board board);
}
