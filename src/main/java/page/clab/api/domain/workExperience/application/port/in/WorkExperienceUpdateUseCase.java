package page.clab.api.domain.workExperience.application.port.in;

import page.clab.api.domain.workExperience.dto.request.WorkExperienceUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface WorkExperienceUpdateUseCase {
    Long update(Long workExperienceId, WorkExperienceUpdateRequestDto requestDto) throws PermissionDeniedException;
}
