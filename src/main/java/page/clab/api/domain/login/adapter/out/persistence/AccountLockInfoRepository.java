package page.clab.api.domain.login.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AccountLockInfoRepository extends JpaRepository<AccountLockInfoJpaEntity, Long> {
    Optional<AccountLockInfoJpaEntity> findByMemberId(String memberId);

    Page<AccountLockInfoJpaEntity> findByLockUntil(LocalDateTime banDate, Pageable pageable);
}
