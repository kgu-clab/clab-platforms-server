package page.clab.api.domain.workExperience.application.port.in;

import page.clab.api.global.exception.PermissionDeniedException;

public interface RemoveWorkExperienceUseCase {
    Long remove(Long workExperienceId) throws PermissionDeniedException;
}
