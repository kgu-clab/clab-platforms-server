package page.clab.api.domain.position.application.port.in;

import page.clab.api.domain.position.dto.response.PositionMyResponseDto;

public interface MyPositionsByYearRetrievalUseCase {
    PositionMyResponseDto retrieve(String year);
}
