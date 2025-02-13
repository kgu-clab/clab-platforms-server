package page.clab.api.domain.memberManagement.award.application.port.in;

import page.clab.api.domain.memberManagement.award.application.dto.request.AwardUpdateRequestDto;

public interface UpdateAwardUseCase {

    Long updateAward(Long awardId, AwardUpdateRequestDto requestDto);
}
