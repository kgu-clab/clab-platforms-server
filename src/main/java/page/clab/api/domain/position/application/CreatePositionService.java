package page.clab.api.domain.position.application;

import page.clab.api.domain.position.dto.request.PositionRequestDto;

public interface CreatePositionService {
    Long execute(PositionRequestDto requestDto);
}
