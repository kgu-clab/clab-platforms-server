package page.clab.api.domain.login.dao;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.login.domain.AccountLockInfo;

@Repository
public interface AccountLockInfoRepository extends JpaRepository<AccountLockInfo, Long> {

    Optional<AccountLockInfo> findByMember_Id(String id);

    Page<AccountLockInfo> findByLockUntil(LocalDateTime banDate, Pageable pageable);

}
