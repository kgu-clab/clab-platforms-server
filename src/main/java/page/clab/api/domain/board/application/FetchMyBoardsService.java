package page.clab.api.domain.board.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.board.dto.response.BoardMyResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface FetchMyBoardsService {
    PagedResponseDto<BoardMyResponseDto> fetchMyBoards(Pageable pageable);
}
