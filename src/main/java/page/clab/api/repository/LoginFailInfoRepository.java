package page.clab.api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.LoginFailInfo;

@Repository
public interface LoginFailInfoRepository extends JpaRepository<LoginFailInfo, Long> {

    Optional<LoginFailInfo> findByMember_Id(String id);

}
