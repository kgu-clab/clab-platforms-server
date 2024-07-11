package page.clab.api.domain.login.application.port.in;

import jakarta.servlet.http.HttpServletRequest;
import page.clab.api.domain.login.domain.AccountAccessResult;

public interface RegisterAccountAccessLogUseCase {
    void registerAccountAccessLog(HttpServletRequest request, String memberId, AccountAccessResult accountAccessResult);
}
