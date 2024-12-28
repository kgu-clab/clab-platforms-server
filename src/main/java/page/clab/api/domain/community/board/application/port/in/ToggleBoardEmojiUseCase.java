package page.clab.api.domain.community.board.application.port.in;

import page.clab.api.domain.community.board.application.dto.response.BoardEmojiToggleResponseDto;

public interface ToggleBoardEmojiUseCase {

    BoardEmojiToggleResponseDto toggleEmojiStatus(Long boardId, String emoji);
}
