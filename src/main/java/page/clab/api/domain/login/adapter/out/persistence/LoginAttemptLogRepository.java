package page.clab.api.domain.login.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.login.domain.LoginAttemptLog;

@Repository
public interface LoginAttemptLogRepository extends JpaRepository<LoginAttemptLog, Long> {
    Page<LoginAttemptLog> findAllByMemberId(String memberId, Pageable pageable);
}
