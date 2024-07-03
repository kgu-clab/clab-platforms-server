package page.clab.api.domain.login.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.login.application.port.out.RegisterAccountLockInfoPort;
import page.clab.api.domain.login.application.port.out.RemoveAccountLockInfoPort;
import page.clab.api.domain.login.application.port.out.RetrieveAccountLockInfoPort;
import page.clab.api.domain.login.application.port.out.UpdateAccountLockInfoPort;
import page.clab.api.domain.login.domain.AccountLockInfo;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountLockInfoPersistenceAdapter implements
        RegisterAccountLockInfoPort,
        RetrieveAccountLockInfoPort,
        UpdateAccountLockInfoPort,
        RemoveAccountLockInfoPort {

    private final AccountLockInfoRepository accountLockInfoRepository;

    @Override
    public AccountLockInfo save(AccountLockInfo accountLockInfo) {
        return accountLockInfoRepository.save(accountLockInfo);
    }

    @Override
    public Optional<AccountLockInfo> findById(Long id) {
        return accountLockInfoRepository.findById(id);
    }

    @Override
    public Optional<AccountLockInfo> findByMemberId(String memberId) {
        return accountLockInfoRepository.findByMemberId(memberId);
    }

    @Override
    public AccountLockInfo update(AccountLockInfo accountLockInfo) {
        return accountLockInfoRepository.save(accountLockInfo);
    }

    @Override
    public void delete(AccountLockInfo accountLockInfo) {
        accountLockInfoRepository.delete(accountLockInfo);
    }

    public Page<AccountLockInfo> findByLockUntil(LocalDateTime banDate, Pageable pageable) {
        return accountLockInfoRepository.findByLockUntil(banDate, pageable);
    }
}
