package page.clab.api.domain.community.board.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.community.board.application.dto.response.BoardListResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveDeletedBoardsUseCase {

    PagedResponseDto<BoardListResponseDto> retrieveDeletedBoards(Pageable pageable);
}
