package page.clab.api.domain.accuse.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.accuse.dto.response.AccuseMyResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveMyAccusationsUseCase {
    PagedResponseDto<AccuseMyResponseDto> retrieveMyAccusations(Pageable pageable);
}
