package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.type.entity.BlacklistIp;

@Repository
public interface BlacklistIpRepository extends JpaRepository<BlacklistIp, Long> {

    boolean existsByIpAddress(String ipAddress);

    void deleteByIpAddress(String ipAddress);

}