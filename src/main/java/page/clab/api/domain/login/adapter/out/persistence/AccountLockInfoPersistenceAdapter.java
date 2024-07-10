package page.clab.api.domain.login.adapter.out.persistence;

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
    private final AccountLockInfoMapper accountLockInfoMapper;

    @Override
    public AccountLockInfo save(AccountLockInfo accountLockInfo) {
        AccountLockInfoJpaEntity jpaEntity = accountLockInfoMapper.toJpaEntity(accountLockInfo);
        AccountLockInfoJpaEntity savedEntity = accountLockInfoRepository.save(jpaEntity);
        return accountLockInfoMapper.toDomainEntity(savedEntity);
    }

    @Override
    public Optional<AccountLockInfo> findById(Long id) {
        return accountLockInfoRepository.findById(id).map(accountLockInfoMapper::toDomainEntity);
    }

    @Override
    public Optional<AccountLockInfo> findByMemberId(String memberId) {
        return accountLockInfoRepository.findByMemberId(memberId).map(accountLockInfoMapper::toDomainEntity);
    }

    @Override
    public AccountLockInfo update(AccountLockInfo accountLockInfo) {
        AccountLockInfoJpaEntity jpaEntity = accountLockInfoMapper.toJpaEntity(accountLockInfo);
        AccountLockInfoJpaEntity updatedEntity = accountLockInfoRepository.save(jpaEntity);
        return accountLockInfoMapper.toDomainEntity(updatedEntity);
    }

    @Override
    public void delete(AccountLockInfo accountLockInfo) {
        AccountLockInfoJpaEntity jpaEntity = accountLockInfoMapper.toJpaEntity(accountLockInfo);
        accountLockInfoRepository.delete(jpaEntity);
    }

    public Page<AccountLockInfo> findByLockUntil(LocalDateTime banDate, Pageable pageable) {
        Page<AccountLockInfoJpaEntity> jpaEntities = accountLockInfoRepository.findByLockUntil(banDate, pageable);
        return jpaEntities.map(accountLockInfoMapper::toDomainEntity);
    }
}
