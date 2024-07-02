package page.clab.api.domain.position.application.port.in;

import page.clab.api.domain.position.dto.request.PositionRequestDto;

public interface PositionRegisterUseCase {
    Long register(PositionRequestDto requestDto);
}
