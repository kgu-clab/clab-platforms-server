package page.clab.api.domain.login.application.port.in;

import page.clab.api.domain.login.domain.AccountLockInfo;

public interface RegisterAccountLockInfoUseCase {
    AccountLockInfo save(String memberId);
}
