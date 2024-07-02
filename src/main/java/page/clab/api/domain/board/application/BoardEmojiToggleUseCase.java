package page.clab.api.domain.board.application;

public interface BoardEmojiToggleUseCase {
    String toggleEmojiStatus(Long boardId, String emoji);
}
