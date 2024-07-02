package page.clab.api.domain.position.application;

import page.clab.api.domain.position.dto.response.PositionMyResponseDto;

public interface MyPositionsByYearRetrievalUseCase {
    PositionMyResponseDto retrieve(String year);
}
