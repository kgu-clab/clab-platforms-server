package page.clab.api.domain.login.application.port.in;

import jakarta.servlet.http.HttpServletRequest;
import page.clab.api.domain.login.domain.LoginAttemptResult;

public interface ManageLoginAttemptLogUseCase {
    void logLoginAttempt(HttpServletRequest request, String memberId, LoginAttemptResult loginAttemptResult);
}
