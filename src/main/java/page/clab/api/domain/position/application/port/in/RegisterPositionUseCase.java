package page.clab.api.domain.position.application.port.in;

import page.clab.api.domain.position.dto.request.PositionRequestDto;

public interface RegisterPositionUseCase {
    Long register(PositionRequestDto requestDto);
}
