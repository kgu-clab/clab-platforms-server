package page.clab.api.domain.login.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.login.domain.LoginAttemptLog;

@Repository
public interface LoginAttemptLogRepository extends JpaRepository<LoginAttemptLog, Long> {

    Page<LoginAttemptLog> findAllByMemberIdOrderByLoginAttemptTimeDesc(String memberId, Pageable pageable);

}
