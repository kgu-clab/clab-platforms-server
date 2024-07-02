package page.clab.api.domain.accuse.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.TargetType;
import page.clab.api.domain.accuse.dto.response.AccuseResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface AccusationRetrievalUseCase {
    PagedResponseDto<AccuseResponseDto> retrieve(TargetType type, AccuseStatus status, boolean countOrder, Pageable pageable);
}
