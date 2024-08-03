package page.clab.api.domain.memberManagement.workExperience.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.memberManagement.workExperience.application.dto.response.WorkExperienceResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveMyWorkExperienceUseCase {
    PagedResponseDto<WorkExperienceResponseDto> retrieveMyWorkExperience(Pageable pageable);
}
