package page.clab.api.domain.workExperience.application.port.in;

import page.clab.api.domain.workExperience.dto.request.WorkExperienceRequestDto;

public interface WorkExperienceRegisterUseCase {
    Long register(WorkExperienceRequestDto requestDto);
}
