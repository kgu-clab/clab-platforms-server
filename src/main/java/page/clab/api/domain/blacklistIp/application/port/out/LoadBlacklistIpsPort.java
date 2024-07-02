package page.clab.api.domain.blacklistIp.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.blacklistIp.domain.BlacklistIp;

import java.util.List;

public interface LoadBlacklistIpsPort {
    List<BlacklistIp> findAll();
    Page<BlacklistIp> findAll(Pageable pageable);
}
