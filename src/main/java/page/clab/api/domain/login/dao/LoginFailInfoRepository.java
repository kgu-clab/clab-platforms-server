package page.clab.api.domain.login.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.login.domain.LoginFailInfo;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface LoginFailInfoRepository extends JpaRepository<LoginFailInfo, Long> {

    Optional<LoginFailInfo> findByMember_Id(String id);

    Page<LoginFailInfo> findByLatestTryLoginDate(LocalDateTime banDate, Pageable pageable);

}
