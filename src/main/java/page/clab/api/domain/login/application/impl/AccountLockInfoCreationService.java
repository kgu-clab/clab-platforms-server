package page.clab.api.domain.login.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.login.application.AccountLockInfoCreationUseCase;
import page.clab.api.domain.login.dao.AccountLockInfoRepository;
import page.clab.api.domain.login.domain.AccountLockInfo;

@Service
@RequiredArgsConstructor
public class AccountLockInfoCreationService implements AccountLockInfoCreationUseCase {

    private final AccountLockInfoRepository accountLockInfoRepository;

    @Override
    public AccountLockInfo create(String memberId) {
        AccountLockInfo accountLockInfo = AccountLockInfo.create(memberId);
        accountLockInfoRepository.save(accountLockInfo);
        return accountLockInfo;
    }
}