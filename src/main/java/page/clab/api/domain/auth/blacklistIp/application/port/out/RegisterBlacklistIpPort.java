package page.clab.api.domain.auth.blacklistIp.application.port.out;

import page.clab.api.domain.auth.blacklistIp.domain.BlacklistIp;

public interface RegisterBlacklistIpPort {
    BlacklistIp save(BlacklistIp blacklistIp);
}
