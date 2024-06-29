package page.clab.api.domain.award.application;

import page.clab.api.global.exception.PermissionDeniedException;

public interface AwardRemoveService {
    Long remove(Long awardId) throws PermissionDeniedException;
}