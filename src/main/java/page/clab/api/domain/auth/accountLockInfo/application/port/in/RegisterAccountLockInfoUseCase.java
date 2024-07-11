package page.clab.api.domain.auth.accountLockInfo.application.port.in;

import page.clab.api.domain.auth.accountLockInfo.domain.AccountLockInfo;

public interface RegisterAccountLockInfoUseCase {
    AccountLockInfo save(String memberId);
}
