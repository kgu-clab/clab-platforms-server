package page.clab.api.domain.login.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountAccessLogRepository extends JpaRepository<AccountAccessLogJpaEntity, Long> {
    Page<AccountAccessLogJpaEntity> findAllByMemberId(String memberId, Pageable pageable);
}
