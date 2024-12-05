package page.clab.api.domain.community.board.application.port.in;

import page.clab.api.domain.community.board.application.dto.response.BoardListResponseDto;

import java.util.List;

public interface RetrieveHotBoardsUseCase {
    List<BoardListResponseDto> retrieveHotBoards();
}
