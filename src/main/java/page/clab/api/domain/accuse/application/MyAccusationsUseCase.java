package page.clab.api.domain.accuse.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.accuse.dto.response.AccuseMyResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface MyAccusationsUseCase {
    PagedResponseDto<AccuseMyResponseDto> retrieve(Pageable pageable);
}
