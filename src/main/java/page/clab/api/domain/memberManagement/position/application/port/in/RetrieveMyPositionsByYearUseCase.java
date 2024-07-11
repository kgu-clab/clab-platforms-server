package page.clab.api.domain.memberManagement.position.application.port.in;

import page.clab.api.domain.memberManagement.position.application.dto.response.PositionMyResponseDto;

public interface RetrieveMyPositionsByYearUseCase {
    PositionMyResponseDto retrieveMyPositionsByYear(String year);
}
