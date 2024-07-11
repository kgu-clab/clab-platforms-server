package page.clab.api.domain.community.board.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.community.board.application.dto.response.BoardMyResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveMyBoardsUseCase {
    PagedResponseDto<BoardMyResponseDto> retrieveMyBoards(Pageable pageable);
}
