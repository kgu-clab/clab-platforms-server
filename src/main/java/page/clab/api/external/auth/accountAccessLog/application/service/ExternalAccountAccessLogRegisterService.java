package page.clab.api.external.auth.accountAccessLog.application.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.auth.accountAccessLog.application.port.in.RegisterAccountAccessLogUseCase;
import page.clab.api.domain.auth.accountAccessLog.domain.AccountAccessResult;
import page.clab.api.external.auth.accountAccessLog.application.port.ExternalRegisterAccountAccessLogUseCase;

@Service
@RequiredArgsConstructor
public class ExternalAccountAccessLogRegisterService implements ExternalRegisterAccountAccessLogUseCase {

    private final RegisterAccountAccessLogUseCase registerAccountAccessLogUseCase;

    @Override
    public void registerAccountAccessLog(HttpServletRequest request, String memberId, AccountAccessResult accountAccessResult) {
        registerAccountAccessLogUseCase.registerAccountAccessLog(request, memberId, accountAccessResult);
    }
}
