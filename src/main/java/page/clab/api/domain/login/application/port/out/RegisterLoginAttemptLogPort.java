package page.clab.api.domain.login.application.port.out;

import page.clab.api.domain.login.domain.LoginAttemptLog;

public interface RegisterLoginAttemptLogPort {
    LoginAttemptLog save(LoginAttemptLog loginAttemptLog);
}
