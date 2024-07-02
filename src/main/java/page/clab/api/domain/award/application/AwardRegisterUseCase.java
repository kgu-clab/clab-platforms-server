package page.clab.api.domain.award.application;

import page.clab.api.domain.award.dto.request.AwardRequestDto;

public interface AwardRegisterUseCase {
    Long register(AwardRequestDto requestDto);
}
