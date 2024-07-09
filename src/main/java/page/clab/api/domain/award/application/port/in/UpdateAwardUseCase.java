package page.clab.api.domain.award.application.port.in;

import page.clab.api.domain.award.application.dto.request.AwardUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface UpdateAwardUseCase {
    Long updateAward(Long awardId, AwardUpdateRequestDto requestDto) throws PermissionDeniedException;
}
