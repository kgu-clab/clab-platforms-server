package page.clab.api.domain.award.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.award.dto.response.AwardResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveAwardsUseCase {
    PagedResponseDto<AwardResponseDto> retrieve(String memberId, Long year, Pageable pageable);
}
