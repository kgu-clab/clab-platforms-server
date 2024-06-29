package page.clab.api.domain.board.application;

public interface BoardEmojiToggleService {
    String toggleEmojiStatus(Long boardId, String emoji);
}
