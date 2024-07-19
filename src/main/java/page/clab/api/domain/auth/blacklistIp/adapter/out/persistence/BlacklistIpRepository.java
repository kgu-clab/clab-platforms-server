package page.clab.api.domain.auth.blacklistIp.adapter.out.persistence;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlacklistIpRepository extends JpaRepository<BlacklistIpJpaEntity, Long> {

    @NotNull
    Page<BlacklistIpJpaEntity> findAll(@NotNull Pageable pageable);

    boolean existsByIpAddress(String ipAddress);

    Optional<BlacklistIpJpaEntity> findByIpAddress(String ipAddress);
}
