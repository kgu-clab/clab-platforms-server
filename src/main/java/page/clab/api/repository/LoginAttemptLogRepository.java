package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.LoginAttemptLog;
import page.clab.api.type.entity.Member;

import java.util.List;

@Repository
public interface LoginAttemptLogRepository extends JpaRepository<LoginAttemptLog, Long> {

    List<LoginAttemptLog> findAllByMember(Member member);

}
