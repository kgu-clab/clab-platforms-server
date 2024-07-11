package page.clab.api.domain.auth.blacklistIp.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.auth.blacklistIp.domain.BlacklistIp;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveBlacklistIpsUseCase {
    PagedResponseDto<BlacklistIp> retrieveBlacklistIps(Pageable pageable);
}
