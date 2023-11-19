package page.clab.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.LoginAttemptLog;
import page.clab.api.type.entity.Member;

@Repository
public interface LoginAttemptLogRepository extends JpaRepository<LoginAttemptLog, Long> {

    Page<LoginAttemptLog> findAllByMemberOrderByLoginAttemptTimeDesc(Member member, Pageable pageable);

}
