package page.clab.api.domain.auth.login.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticatorRepository extends JpaRepository<AuthenticatorJpaEntity, String> {
}
