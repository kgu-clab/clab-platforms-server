package page.clab.api.domain.memberManagement.cloud.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.memberManagement.cloud.application.dto.response.CloudUsageInfo;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveAllCloudUsageUseCase {
    PagedResponseDto<CloudUsageInfo> retrieveAllCloudUsages(Pageable pageable);
}
