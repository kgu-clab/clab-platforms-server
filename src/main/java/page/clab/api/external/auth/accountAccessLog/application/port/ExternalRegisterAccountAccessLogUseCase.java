package page.clab.api.external.auth.accountAccessLog.application.port;

import jakarta.servlet.http.HttpServletRequest;
import page.clab.api.domain.auth.accountAccessLog.domain.AccountAccessResult;

public interface ExternalRegisterAccountAccessLogUseCase {
    void registerAccountAccessLog(HttpServletRequest request, String memberId, AccountAccessResult accountAccessResult);
}
