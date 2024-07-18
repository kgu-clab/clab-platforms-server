package page.clab.api.external.auth.blacklistIp.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.auth.blacklistIp.application.port.out.RegisterBlacklistIpPort;
import page.clab.api.domain.auth.blacklistIp.domain.BlacklistIp;
import page.clab.api.external.auth.blacklistIp.application.port.ExternalRegisterBlacklistIpUseCase;

@Service
@RequiredArgsConstructor
public class ExternalBlacklistIpRegisterService implements ExternalRegisterBlacklistIpUseCase {

    private final RegisterBlacklistIpPort registerBlacklistIpPort;

    @Transactional
    @Override
    public void save(BlacklistIp blacklistIp) {
        registerBlacklistIpPort.save(blacklistIp);
    }
}
