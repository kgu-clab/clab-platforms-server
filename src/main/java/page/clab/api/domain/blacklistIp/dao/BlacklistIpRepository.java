package page.clab.api.domain.blacklistIp.dao;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.blacklistIp.domain.BlacklistIp;

import java.util.Optional;

@Repository
public interface BlacklistIpRepository extends JpaRepository<BlacklistIp, Long> {

    @NotNull
    Page<BlacklistIp> findAll(@NotNull Pageable pageable);

    boolean existsByIpAddress(String ipAddress);

    Optional<BlacklistIp> findByIpAddress(String ipAddress);
}