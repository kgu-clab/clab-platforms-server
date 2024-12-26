package page.clab.api.domain.memberManagement.award.application.port.in;

import page.clab.api.domain.memberManagement.award.application.dto.request.AwardRequestDto;

public interface RegisterAwardUseCase {

    Long registerAward(AwardRequestDto requestDto);
}
