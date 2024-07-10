package page.clab.api.domain.login.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticatorRepository extends JpaRepository<AuthenticatorJpaEntity, String> {
}
