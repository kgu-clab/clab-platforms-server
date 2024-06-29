package page.clab.api.domain.workExperience.application;

import page.clab.api.global.exception.PermissionDeniedException;

public interface WorkExperienceRemoveService {
    Long remove(Long workExperienceId) throws PermissionDeniedException;
}
