package page.clab.api.domain.workExperience.application;

import page.clab.api.domain.workExperience.dto.request.WorkExperienceUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface UpdateWorkExperienceService {
    Long updateWorkExperience(Long workExperienceId, WorkExperienceUpdateRequestDto requestDto) throws PermissionDeniedException;
}
