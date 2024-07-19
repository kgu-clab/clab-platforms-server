package page.clab.api.domain.auth.blacklistIp.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.auth.blacklistIp.application.dto.response.BlacklistIpResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveBlacklistIpsUseCase {
    PagedResponseDto<BlacklistIpResponseDto> retrieveBlacklistIps(Pageable pageable);
}
