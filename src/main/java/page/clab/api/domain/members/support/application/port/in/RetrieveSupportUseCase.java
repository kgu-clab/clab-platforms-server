package page.clab.api.domain.members.support.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.members.support.application.dto.response.SupportListResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveSupportUseCase {
    PagedResponseDto<SupportListResponseDto> retrieveSupports(Pageable pageable);
}
