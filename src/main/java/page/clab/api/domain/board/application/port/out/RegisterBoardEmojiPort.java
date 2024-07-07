package page.clab.api.domain.board.application.port.out;

import page.clab.api.domain.board.domain.BoardEmoji;

public interface RegisterBoardEmojiPort {
    BoardEmoji save(BoardEmoji boardEmoji);
}
