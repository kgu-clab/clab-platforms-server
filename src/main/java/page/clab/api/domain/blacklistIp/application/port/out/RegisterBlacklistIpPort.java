package page.clab.api.domain.blacklistIp.application.port.out;

import page.clab.api.domain.blacklistIp.domain.BlacklistIp;

public interface RegisterBlacklistIpPort {
    BlacklistIp save(BlacklistIp blacklistIp);
}
