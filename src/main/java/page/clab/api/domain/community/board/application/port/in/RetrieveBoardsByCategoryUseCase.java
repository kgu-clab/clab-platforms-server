package page.clab.api.domain.community.board.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.community.board.application.dto.response.BoardOverviewResponseDto;
import page.clab.api.domain.community.board.domain.BoardCategory;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveBoardsByCategoryUseCase {

    PagedResponseDto<BoardOverviewResponseDto> retrieveBoardsByCategory(BoardCategory category, Pageable pageable);
}
