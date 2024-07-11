package page.clab.api.domain.auth.accountLockInfo.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.auth.accountLockInfo.application.port.in.RegisterAccountLockInfoUseCase;
import page.clab.api.domain.auth.accountLockInfo.application.port.out.RegisterAccountLockInfoPort;
import page.clab.api.domain.auth.accountLockInfo.domain.AccountLockInfo;

@Service
@RequiredArgsConstructor
public class AccountLockInfoRegisterService implements RegisterAccountLockInfoUseCase {

    private final RegisterAccountLockInfoPort registerAccountLockInfoPort;

    @Override
    public AccountLockInfo save(String memberId) {
        AccountLockInfo accountLockInfo = AccountLockInfo.create(memberId);
        return registerAccountLockInfoPort.save(accountLockInfo);
    }
}
