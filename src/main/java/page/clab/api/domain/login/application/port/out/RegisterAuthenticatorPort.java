package page.clab.api.domain.login.application.port.out;

import page.clab.api.domain.login.domain.Authenticator;

public interface RegisterAuthenticatorPort {
    Authenticator save(Authenticator authenticator);
}
