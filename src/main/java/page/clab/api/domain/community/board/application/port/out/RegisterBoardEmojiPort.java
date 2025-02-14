package page.clab.api.domain.community.board.application.port.out;


import page.clab.api.domain.community.board.domain.BoardEmoji;

public interface RegisterBoardEmojiPort {

    BoardEmoji save(BoardEmoji boardEmoji);
}
