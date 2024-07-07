package page.clab.api.domain.blacklistIp.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blacklistIp.application.port.in.RetrieveBlacklistIpsUseCase;
import page.clab.api.domain.blacklistIp.application.port.out.RetrieveBlacklistIpPort;
import page.clab.api.domain.blacklistIp.domain.BlacklistIp;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class BlacklistIpRetrievalService implements RetrieveBlacklistIpsUseCase {

    private final RetrieveBlacklistIpPort retrieveBlacklistIpPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<BlacklistIp> retrieveBlacklistIps(Pageable pageable) {
        Page<BlacklistIp> blacklistedIps = retrieveBlacklistIpPort.findAll(pageable);
        return new PagedResponseDto<>(blacklistedIps);
    }
}
