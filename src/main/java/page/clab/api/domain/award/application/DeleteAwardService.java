package page.clab.api.domain.award.application;

import page.clab.api.global.exception.PermissionDeniedException;

public interface DeleteAwardService {
    Long execute(Long awardId) throws PermissionDeniedException;
}