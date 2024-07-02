package page.clab.api.domain.award.application.port.in;

import page.clab.api.domain.award.dto.request.AwardRequestDto;

public interface AwardRegisterUseCase {
    Long register(AwardRequestDto requestDto);
}
