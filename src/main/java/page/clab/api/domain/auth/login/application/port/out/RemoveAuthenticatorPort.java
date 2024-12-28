package page.clab.api.domain.auth.login.application.port.out;

import page.clab.api.domain.auth.login.domain.Authenticator;

public interface RemoveAuthenticatorPort {

    void delete(Authenticator authenticator);
}
