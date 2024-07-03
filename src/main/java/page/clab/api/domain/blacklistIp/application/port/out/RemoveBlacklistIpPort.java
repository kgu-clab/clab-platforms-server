package page.clab.api.domain.blacklistIp.application.port.out;

import page.clab.api.domain.blacklistIp.domain.BlacklistIp;

public interface RemoveBlacklistIpPort {
    void delete(BlacklistIp blacklistIp);
    void deleteAll();
}
