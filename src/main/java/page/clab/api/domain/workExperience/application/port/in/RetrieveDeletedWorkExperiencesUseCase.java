package page.clab.api.domain.workExperience.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.workExperience.dto.response.WorkExperienceResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveDeletedWorkExperiencesUseCase {
    PagedResponseDto<WorkExperienceResponseDto> retrieveDeletedWorkExperiences(Pageable pageable);
}
