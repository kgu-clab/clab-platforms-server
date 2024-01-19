package page.clab.api.domain.login.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.login.domain.LoginFailInfo;

@Repository
public interface LoginFailInfoRepository extends JpaRepository<LoginFailInfo, Long> {

    Optional<LoginFailInfo> findByMember_Id(String id);

}
