package page.clab.api.domain.board.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.board.dto.response.BoardListResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface DeletedBoardsRetrievalUseCase {
    PagedResponseDto<BoardListResponseDto> retrieve(Pageable pageable);
}