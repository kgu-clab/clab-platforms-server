package page.clab.api.external.auth.blacklistIp.application.port;

import page.clab.api.domain.auth.blacklistIp.domain.BlacklistIp;

public interface ExternalRegisterBlacklistIpUseCase {

    void save(BlacklistIp blacklistIp);
}
