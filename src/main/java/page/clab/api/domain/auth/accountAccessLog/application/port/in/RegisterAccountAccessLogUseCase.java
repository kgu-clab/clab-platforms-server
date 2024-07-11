package page.clab.api.domain.auth.accountAccessLog.application.port.in;

import jakarta.servlet.http.HttpServletRequest;
import page.clab.api.domain.auth.accountAccessLog.domain.AccountAccessResult;

public interface RegisterAccountAccessLogUseCase {
    void registerAccountAccessLog(HttpServletRequest request, String memberId, AccountAccessResult accountAccessResult);
}
