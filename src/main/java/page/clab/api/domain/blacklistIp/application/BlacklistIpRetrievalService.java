package page.clab.api.domain.blacklistIp.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.blacklistIp.domain.BlacklistIp;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface BlacklistIpRetrievalService {
    PagedResponseDto<BlacklistIp> retrieve(Pageable pageable);
}
