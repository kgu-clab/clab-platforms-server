package page.clab.api.domain.auth.accountLockInfo.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountLockInfoRepository extends JpaRepository<AccountLockInfoJpaEntity, Long> {

    Optional<AccountLockInfoJpaEntity> findByMemberId(String memberId);

    Page<AccountLockInfoJpaEntity> findByLockUntil(LocalDateTime banDate, Pageable pageable);
}
