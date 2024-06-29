package page.clab.api.domain.login.application;

import page.clab.api.domain.login.domain.AccountLockInfo;

public interface CreateAccountLockInfoService {
    AccountLockInfo execute(String memberId);
}
