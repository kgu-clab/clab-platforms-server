package page.clab.api.domain.login.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.login.application.port.in.CreateAccountLockInfoUseCase;
import page.clab.api.domain.login.application.port.out.RegisterAccountLockInfoPort;
import page.clab.api.domain.login.domain.AccountLockInfo;

@Service
@RequiredArgsConstructor
public class AccountLockInfoCreationService implements CreateAccountLockInfoUseCase {

    private final RegisterAccountLockInfoPort registerAccountLockInfoPort;

    @Override
    public AccountLockInfo create(String memberId) {
        AccountLockInfo accountLockInfo = AccountLockInfo.create(memberId);
        return registerAccountLockInfoPort.save(accountLockInfo);
    }
}
