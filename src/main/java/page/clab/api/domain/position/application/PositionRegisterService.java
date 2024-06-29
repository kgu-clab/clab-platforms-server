package page.clab.api.domain.position.application;

import page.clab.api.domain.position.dto.request.PositionRequestDto;

public interface PositionRegisterService {
    Long register(PositionRequestDto requestDto);
}
