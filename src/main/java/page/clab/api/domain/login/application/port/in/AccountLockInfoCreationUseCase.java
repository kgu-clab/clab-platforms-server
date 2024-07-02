package page.clab.api.domain.login.application.port.in;

import page.clab.api.domain.login.domain.AccountLockInfo;

public interface AccountLockInfoCreationUseCase {
    AccountLockInfo create(String memberId);
}
