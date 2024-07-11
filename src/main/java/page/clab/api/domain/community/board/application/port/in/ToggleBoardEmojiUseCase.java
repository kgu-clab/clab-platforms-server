package page.clab.api.domain.community.board.application.port.in;

public interface ToggleBoardEmojiUseCase {
    String toggleEmojiStatus(Long boardId, String emoji);
}
