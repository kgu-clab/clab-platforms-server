package page.clab.api.domain.community.board.application.port.in;

import page.clab.api.domain.community.board.application.dto.request.BoardUpdateRequestDto;

public interface UpdateBoardUseCase {

    String updateBoard(Long boardId, BoardUpdateRequestDto requestDto);
}
