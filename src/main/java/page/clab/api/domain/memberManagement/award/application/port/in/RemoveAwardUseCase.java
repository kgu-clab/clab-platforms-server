package page.clab.api.domain.memberManagement.award.application.port.in;

import page.clab.api.global.exception.PermissionDeniedException;

public interface RemoveAwardUseCase {
    Long removeAward(Long awardId) throws PermissionDeniedException;
}
