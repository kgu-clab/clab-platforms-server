package page.clab.api.domain.login.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.login.application.CreateAccountLockInfoService;
import page.clab.api.domain.login.dao.AccountLockInfoRepository;
import page.clab.api.domain.login.domain.AccountLockInfo;

@Service
@RequiredArgsConstructor
public class CreateAccountLockInfoServiceImpl implements CreateAccountLockInfoService {

    private final AccountLockInfoRepository accountLockInfoRepository;

    @Override
    public AccountLockInfo execute(String memberId) {
        AccountLockInfo accountLockInfo = AccountLockInfo.create(memberId);
        accountLockInfoRepository.save(accountLockInfo);
        return accountLockInfo;
    }
}