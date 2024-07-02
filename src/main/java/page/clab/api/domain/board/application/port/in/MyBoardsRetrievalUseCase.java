package page.clab.api.domain.board.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.board.dto.response.BoardMyResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface MyBoardsRetrievalUseCase {
    PagedResponseDto<BoardMyResponseDto> retrieve(Pageable pageable);
}
