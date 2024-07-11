package page.clab.api.domain.login.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.login.application.port.in.RegisterAccountLockInfoUseCase;
import page.clab.api.domain.login.application.port.out.RegisterAccountLockInfoPort;
import page.clab.api.domain.login.domain.AccountLockInfo;

@Service
@RequiredArgsConstructor
public class AccountLockInfoCreationService implements RegisterAccountLockInfoUseCase {

    private final RegisterAccountLockInfoPort registerAccountLockInfoPort;

    @Override
    public AccountLockInfo save(String memberId) {
        AccountLockInfo accountLockInfo = AccountLockInfo.create(memberId);
        return registerAccountLockInfoPort.save(accountLockInfo);
    }
}
