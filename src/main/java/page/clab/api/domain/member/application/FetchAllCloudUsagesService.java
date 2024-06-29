package page.clab.api.domain.member.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.member.dto.response.CloudUsageInfo;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface FetchAllCloudUsagesService {
    PagedResponseDto<CloudUsageInfo> execute(Pageable pageable);
}
