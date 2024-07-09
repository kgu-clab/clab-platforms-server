package page.clab.api.domain.position.application.port.in;

import page.clab.api.domain.position.application.dto.response.PositionMyResponseDto;

public interface RetrieveMyPositionsByYearUseCase {
    PositionMyResponseDto retrieveMyPositionsByYear(String year);
}
