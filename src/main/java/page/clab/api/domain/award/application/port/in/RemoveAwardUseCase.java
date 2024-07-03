package page.clab.api.domain.award.application.port.in;

import page.clab.api.global.exception.PermissionDeniedException;

public interface RemoveAwardUseCase {
    Long remove(Long awardId) throws PermissionDeniedException;
}
