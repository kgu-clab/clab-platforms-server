package page.clab.api.domain.login.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.login.domain.AccountLockInfo;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AccountLockInfoRepository extends JpaRepository<AccountLockInfo, Long> {
    Optional<AccountLockInfo> findByMemberId(String memberId);

    Page<AccountLockInfo> findByLockUntil(LocalDateTime banDate, Pageable pageable);
}
