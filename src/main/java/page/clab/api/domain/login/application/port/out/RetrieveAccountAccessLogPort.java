package page.clab.api.domain.login.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.login.domain.AccountAccessLog;

public interface RetrieveAccountAccessLogPort {
    Page<AccountAccessLog> findAllByMemberId(String memberId, Pageable pageable);
}
