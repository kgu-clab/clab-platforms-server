package page.clab.api.domain.board.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.board.dto.response.BoardListResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface FetchDeletedBoardsService {
    PagedResponseDto<BoardListResponseDto> fetchDeletedBoards(Pageable pageable);
}