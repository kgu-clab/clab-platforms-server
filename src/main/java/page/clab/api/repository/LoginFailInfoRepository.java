package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.LoginFailInfo;

import java.util.Optional;

@Repository
public interface LoginFailInfoRepository extends JpaRepository<LoginFailInfo, Long> {

    Optional<LoginFailInfo> findByUser_Id(String id);

}
