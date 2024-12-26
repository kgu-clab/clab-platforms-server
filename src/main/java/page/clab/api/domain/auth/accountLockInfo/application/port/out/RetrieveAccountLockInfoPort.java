package page.clab.api.domain.auth.accountLockInfo.application.port.out;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.auth.accountLockInfo.domain.AccountLockInfo;

public interface RetrieveAccountLockInfoPort {

    Optional<AccountLockInfo> findByMemberId(String memberId);

    Page<AccountLockInfo> findByLockUntil(LocalDateTime lockUntil, Pageable pageable);
}
