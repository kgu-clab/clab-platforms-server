package page.clab.api.domain.award.application.port.in;

import page.clab.api.global.exception.PermissionDeniedException;

public interface AwardRemoveUseCase {
    Long remove(Long awardId) throws PermissionDeniedException;
}