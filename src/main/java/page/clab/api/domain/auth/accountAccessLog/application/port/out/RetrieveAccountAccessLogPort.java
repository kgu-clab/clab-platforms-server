package page.clab.api.domain.auth.accountAccessLog.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.auth.accountAccessLog.domain.AccountAccessLog;

public interface RetrieveAccountAccessLogPort {
    Page<AccountAccessLog> findAllByMemberId(String memberId, Pageable pageable);
}
