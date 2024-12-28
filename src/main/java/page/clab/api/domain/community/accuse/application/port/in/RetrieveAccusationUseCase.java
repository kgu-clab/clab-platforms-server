package page.clab.api.domain.community.accuse.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.community.accuse.application.dto.response.AccuseResponseDto;
import page.clab.api.domain.community.accuse.domain.AccuseStatus;
import page.clab.api.domain.community.accuse.domain.TargetType;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveAccusationUseCase {

    PagedResponseDto<AccuseResponseDto> retrieveAccusations(TargetType type, AccuseStatus status, boolean countOrder,
        Pageable pageable);
}
