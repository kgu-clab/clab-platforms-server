package page.clab.api.domain.login.application.port.out;

import page.clab.api.domain.login.domain.Authenticator;

public interface RemoveAuthenticatorPort {
    void delete(Authenticator authenticator);
}
