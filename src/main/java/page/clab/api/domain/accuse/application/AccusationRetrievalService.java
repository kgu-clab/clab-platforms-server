package page.clab.api.domain.accuse.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.TargetType;
import page.clab.api.domain.accuse.dto.response.AccuseResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface AccusationRetrievalService {
    PagedResponseDto<AccuseResponseDto> retrieve(TargetType type, AccuseStatus status, boolean countOrder, Pageable pageable);
}
