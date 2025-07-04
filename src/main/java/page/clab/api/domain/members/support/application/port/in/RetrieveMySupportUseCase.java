package page.clab.api.domain.members.support.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.members.support.application.dto.response.SupportMyResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveMySupportUseCase {
    PagedResponseDto<SupportMyResponseDto> retrieveMySupports(Pageable pageable);
}
