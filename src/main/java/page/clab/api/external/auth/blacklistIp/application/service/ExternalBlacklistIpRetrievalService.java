package page.clab.api.external.auth.blacklistIp.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.auth.blacklistIp.application.port.out.RetrieveBlacklistIpPort;
import page.clab.api.external.auth.blacklistIp.application.port.ExternalRetrieveBlacklistIpUseCase;

@Service
@RequiredArgsConstructor
public class ExternalBlacklistIpRetrievalService implements ExternalRetrieveBlacklistIpUseCase {

    private final RetrieveBlacklistIpPort retrieveBlacklistIpPort;

    @Transactional(readOnly = true)
    @Override
    public boolean existsByIpAddress(String clientIpAddress) {
        return retrieveBlacklistIpPort.existsByIpAddress(clientIpAddress);
    }
}
