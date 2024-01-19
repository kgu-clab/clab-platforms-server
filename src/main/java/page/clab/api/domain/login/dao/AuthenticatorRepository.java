package page.clab.api.domain.login.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.login.domain.Authenticator;

@Repository
public interface AuthenticatorRepository extends JpaRepository<Authenticator, String> {

}
