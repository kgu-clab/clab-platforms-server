package page.clab.api.domain.memberManagement.workExperience.application.port.in;

import page.clab.api.domain.memberManagement.workExperience.application.dto.request.WorkExperienceRequestDto;

public interface RegisterWorkExperienceUseCase {
    Long registerWorkExperience(WorkExperienceRequestDto requestDto);
}
