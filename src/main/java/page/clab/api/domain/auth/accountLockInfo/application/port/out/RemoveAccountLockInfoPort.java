package page.clab.api.domain.auth.accountLockInfo.application.port.out;

import page.clab.api.domain.auth.accountLockInfo.domain.AccountLockInfo;

public interface RemoveAccountLockInfoPort {

    void delete(AccountLockInfo accountLockInfo);
}
