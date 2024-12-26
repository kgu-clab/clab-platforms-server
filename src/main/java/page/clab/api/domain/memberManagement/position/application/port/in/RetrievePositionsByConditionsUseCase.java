package page.clab.api.domain.memberManagement.position.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.memberManagement.position.application.dto.response.PositionResponseDto;
import page.clab.api.domain.memberManagement.position.domain.PositionType;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrievePositionsByConditionsUseCase {

    PagedResponseDto<PositionResponseDto> retrievePositions(String year, PositionType positionType, Pageable pageable);
}
