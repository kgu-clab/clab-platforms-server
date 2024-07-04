package page.clab.api.domain.award.application.port.in;

import page.clab.api.domain.award.dto.request.AwardRequestDto;

public interface RegisterAwardUseCase {
    Long registerAward(AwardRequestDto requestDto);
}
