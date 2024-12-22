package page.clab.api.domain.memberManagement.award.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.memberManagement.award.application.dto.response.AwardResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveDeletedAwardsUseCase {

    PagedResponseDto<AwardResponseDto> retrieveDeletedAwards(Pageable pageable);
}
