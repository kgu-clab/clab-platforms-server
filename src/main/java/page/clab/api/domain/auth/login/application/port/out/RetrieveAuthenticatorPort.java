package page.clab.api.domain.auth.login.application.port.out;

import java.util.Optional;
import page.clab.api.domain.auth.login.domain.Authenticator;

public interface RetrieveAuthenticatorPort {

    Optional<Authenticator> findById(String memberId);

    Authenticator getById(String memberId);

    boolean existsById(String memberId);
}
