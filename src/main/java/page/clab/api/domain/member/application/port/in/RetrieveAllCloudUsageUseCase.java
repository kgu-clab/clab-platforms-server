package page.clab.api.domain.member.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.member.dto.response.CloudUsageInfo;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveAllCloudUsageUseCase {
    PagedResponseDto<CloudUsageInfo> retrieveAllCloudUsages(Pageable pageable);
}
