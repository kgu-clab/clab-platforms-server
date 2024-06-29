package page.clab.api.domain.workExperience.application;

import page.clab.api.domain.workExperience.dto.request.WorkExperienceRequestDto;

public interface CreateWorkExperienceService {
    Long createWorkExperience(WorkExperienceRequestDto requestDto);
}
