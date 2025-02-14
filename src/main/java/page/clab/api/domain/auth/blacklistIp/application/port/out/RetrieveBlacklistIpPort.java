package page.clab.api.domain.auth.blacklistIp.application.port.out;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.auth.blacklistIp.domain.BlacklistIp;

public interface RetrieveBlacklistIpPort {

    List<BlacklistIp> findAll();

    Page<BlacklistIp> findAll(Pageable pageable);

    Optional<BlacklistIp> findByIpAddress(String ipAddress);

    BlacklistIp getByIpAddress(String ipAddress);

    boolean existsByIpAddress(String clientIpAddress);
}
