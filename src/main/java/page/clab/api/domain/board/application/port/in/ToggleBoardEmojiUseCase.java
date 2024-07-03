package page.clab.api.domain.board.application.port.in;

public interface ToggleBoardEmojiUseCase {
    String toggleEmojiStatus(Long boardId, String emoji);
}
