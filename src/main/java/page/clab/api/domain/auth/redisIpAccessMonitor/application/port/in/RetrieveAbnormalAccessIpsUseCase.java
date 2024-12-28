package page.clab.api.domain.auth.redisIpAccessMonitor.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.auth.redisIpAccessMonitor.domain.RedisIpAccessMonitor;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveAbnormalAccessIpsUseCase {

    PagedResponseDto<RedisIpAccessMonitor> retrieveAbnormalAccessIps(Pageable pageable);
}
