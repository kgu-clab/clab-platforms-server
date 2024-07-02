package page.clab.api.domain.blacklistIp.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.blacklistIp.application.BlacklistIpRetrievalUseCase;
import page.clab.api.domain.blacklistIp.dao.BlacklistIpRepository;
import page.clab.api.domain.blacklistIp.domain.BlacklistIp;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class BlacklistIpRetrievalService implements BlacklistIpRetrievalUseCase {

    private final BlacklistIpRepository blacklistIpRepository;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<BlacklistIp> retrieve(Pageable pageable) {
        Page<BlacklistIp> blacklistedIps = blacklistIpRepository.findAll(pageable);
        return new PagedResponseDto<>(blacklistedIps);
    }
}
