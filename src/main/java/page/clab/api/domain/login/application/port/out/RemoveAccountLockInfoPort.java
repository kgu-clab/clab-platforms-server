package page.clab.api.domain.login.application.port.out;

import page.clab.api.domain.login.domain.AccountLockInfo;

public interface RemoveAccountLockInfoPort {
    void delete(AccountLockInfo accountLockInfo);
}
