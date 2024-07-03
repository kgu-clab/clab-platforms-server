package page.clab.api.domain.application.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.application.dto.response.ApplicationResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveDeletedApplicationsUseCase {
    PagedResponseDto<ApplicationResponseDto> retrieve(Pageable pageable);
}
