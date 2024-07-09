package page.clab.api.domain.login.application.port.out;

import page.clab.api.domain.login.domain.AccountLockInfo;

public interface RegisterAccountLockInfoPort {
    AccountLockInfo save(AccountLockInfo accountLockInfo);
}
