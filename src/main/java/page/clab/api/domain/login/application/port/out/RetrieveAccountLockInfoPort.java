package page.clab.api.domain.login.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.login.domain.AccountLockInfo;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RetrieveAccountLockInfoPort {
    Optional<AccountLockInfo> findById(Long id);

    Optional<AccountLockInfo> findByMemberId(String memberId);

    Page<AccountLockInfo> findByLockUntil(LocalDateTime lockUntil, Pageable pageable);
}
