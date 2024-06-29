package page.clab.api.domain.login.application;

import jakarta.servlet.http.HttpServletRequest;
import page.clab.api.domain.login.domain.LoginAttemptResult;

public interface CreateLoginAttemptLogService {
    void execute(HttpServletRequest request, String memberId, LoginAttemptResult loginAttemptResult);
}