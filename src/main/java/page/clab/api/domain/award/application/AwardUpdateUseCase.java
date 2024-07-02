package page.clab.api.domain.award.application;

import page.clab.api.domain.award.dto.request.AwardUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface AwardUpdateUseCase {
    Long update(Long awardId, AwardUpdateRequestDto requestDto) throws PermissionDeniedException;
}