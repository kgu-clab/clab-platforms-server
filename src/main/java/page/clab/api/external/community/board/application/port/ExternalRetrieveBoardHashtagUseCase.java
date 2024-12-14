package page.clab.api.external.community.board.application.port;

import java.util.List;
import page.clab.api.domain.community.board.application.dto.response.BoardHashtagResponseDto;

public interface ExternalRetrieveBoardHashtagUseCase {
    List<BoardHashtagResponseDto> getAllByBoardId(Long boardId);
}
