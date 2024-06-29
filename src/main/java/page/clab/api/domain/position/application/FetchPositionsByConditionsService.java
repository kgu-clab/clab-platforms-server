package page.clab.api.domain.position.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.position.domain.PositionType;
import page.clab.api.domain.position.dto.response.PositionResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface FetchPositionsByConditionsService {
    PagedResponseDto<PositionResponseDto> execute(String year, PositionType positionType, Pageable pageable);
}
