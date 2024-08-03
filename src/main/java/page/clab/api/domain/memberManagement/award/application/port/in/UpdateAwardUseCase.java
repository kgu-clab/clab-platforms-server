package page.clab.api.domain.memberManagement.award.application.port.in;

import page.clab.api.domain.memberManagement.award.application.dto.request.AwardUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface UpdateAwardUseCase {
    Long updateAward(Long awardId, AwardUpdateRequestDto requestDto) throws PermissionDeniedException;
}
