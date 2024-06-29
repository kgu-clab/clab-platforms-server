package page.clab.api.domain.position.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.position.dto.response.PositionResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface FetchDeletedPositionsService {
    PagedResponseDto<PositionResponseDto> execute(Pageable pageable);
}
