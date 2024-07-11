package page.clab.api.domain.blacklistIp.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.blacklistIp.domain.BlacklistIp;

import java.util.List;
import java.util.Optional;

public interface RetrieveBlacklistIpPort {
    List<BlacklistIp> findAll();

    Page<BlacklistIp> findAll(Pageable pageable);

    Optional<BlacklistIp> findByIpAddress(String ipAddress);

    BlacklistIp findByIpAddressOrThrow(String ipAddress);

    boolean existsByIpAddress(String clientIpAddress);
}
