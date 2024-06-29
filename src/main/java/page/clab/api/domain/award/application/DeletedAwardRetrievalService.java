package page.clab.api.domain.award.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.award.dto.response.AwardResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface DeletedAwardRetrievalService {
    PagedResponseDto<AwardResponseDto> retrieve(Pageable pageable);
}