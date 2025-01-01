package page.clab.api.domain.community.board.application.port.in;

import java.util.List;
import page.clab.api.domain.community.board.application.dto.response.BoardListResponseDto;

public interface RetrieveHotBoardsUseCase {
    List<BoardListResponseDto> retrieveHotBoards(String strategyName);
}
