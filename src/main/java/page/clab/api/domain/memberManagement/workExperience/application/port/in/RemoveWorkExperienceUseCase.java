package page.clab.api.domain.memberManagement.workExperience.application.port.in;

import page.clab.api.global.exception.PermissionDeniedException;

public interface RemoveWorkExperienceUseCase {

    Long removeWorkExperience(Long workExperienceId) throws PermissionDeniedException;
}
