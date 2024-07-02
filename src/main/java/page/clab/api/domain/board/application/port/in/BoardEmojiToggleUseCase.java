package page.clab.api.domain.board.application.port.in;

public interface BoardEmojiToggleUseCase {
    String toggleEmojiStatus(Long boardId, String emoji);
}
