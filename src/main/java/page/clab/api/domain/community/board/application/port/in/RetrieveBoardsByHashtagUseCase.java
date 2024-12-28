package page.clab.api.domain.community.board.application.port.in;

import java.util.List;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.community.board.application.dto.response.BoardOverviewResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveBoardsByHashtagUseCase {

    PagedResponseDto<BoardOverviewResponseDto> retrieveBoardsByHashtag(List<String> hashtags, Pageable pageable);
}
