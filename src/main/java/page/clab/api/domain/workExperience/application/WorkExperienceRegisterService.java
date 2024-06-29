package page.clab.api.domain.workExperience.application;

import page.clab.api.domain.workExperience.dto.request.WorkExperienceRequestDto;

public interface WorkExperienceRegisterService {
    Long register(WorkExperienceRequestDto requestDto);
}
