package page.clab.api.domain.login.application.port.out;

import page.clab.api.domain.login.domain.Authenticator;

import java.util.Optional;

public interface RetrieveAuthenticatorPort {
    Optional<Authenticator> findById(String memberId);

    Authenticator findByIdOrThrow(String memberId);

    boolean existsById(String memberId);
}
