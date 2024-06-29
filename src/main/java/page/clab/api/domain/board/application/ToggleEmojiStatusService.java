package page.clab.api.domain.board.application;

public interface ToggleEmojiStatusService {
    String toggleEmojiStatus(Long boardId, String emoji);
}
