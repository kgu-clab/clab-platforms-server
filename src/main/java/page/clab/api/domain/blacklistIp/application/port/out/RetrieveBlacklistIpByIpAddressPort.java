package page.clab.api.domain.blacklistIp.application.port.out;

import page.clab.api.domain.blacklistIp.domain.BlacklistIp;

import java.util.Optional;

public interface RetrieveBlacklistIpByIpAddressPort {
    Optional<BlacklistIp> findByIpAddress(String ipAddress);
    BlacklistIp findByIpAddressOrThrow(String ipAddress);
}