package page.clab.api.domain.workExperience.application;

import page.clab.api.global.exception.PermissionDeniedException;

public interface DeleteWorkExperienceService {
    Long deleteWorkExperience(Long workExperienceId) throws PermissionDeniedException;
}
