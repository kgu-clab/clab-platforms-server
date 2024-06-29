package page.clab.api.domain.blacklistIp.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.blacklistIp.domain.BlacklistIp;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface FetchBlacklistIpsService {
    PagedResponseDto<BlacklistIp> execute(Pageable pageable);
}
