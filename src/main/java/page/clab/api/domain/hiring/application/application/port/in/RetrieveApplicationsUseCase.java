package page.clab.api.domain.hiring.application.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.hiring.application.application.dto.response.ApplicationResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveApplicationsUseCase {
    PagedResponseDto<ApplicationResponseDto> retrieveApplications(Long recruitmentId, String studentId, Boolean isPass, Pageable pageable);
}
