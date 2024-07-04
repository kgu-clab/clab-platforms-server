package page.clab.api.domain.workExperience.application.port.in;

import page.clab.api.global.exception.PermissionDeniedException;

public interface RemoveWorkExperienceUseCase {
    Long removeWorkExperience(Long workExperienceId) throws PermissionDeniedException;
}
