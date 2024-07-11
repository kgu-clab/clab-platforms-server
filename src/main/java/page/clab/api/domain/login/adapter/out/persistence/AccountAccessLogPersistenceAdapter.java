package page.clab.api.domain.login.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.login.application.port.out.RegisterAccountAccessLogPort;
import page.clab.api.domain.login.application.port.out.RetrieveAccountAccessLogPort;
import page.clab.api.domain.login.domain.AccountAccessLog;

@Component
@RequiredArgsConstructor
public class AccountAccessLogPersistenceAdapter implements
        RegisterAccountAccessLogPort,
        RetrieveAccountAccessLogPort {

    private final AccountAccessLogRepository accountAccessLogRepository;
    private final AccountAccessLogMapper accountAccessLogMapper;

    @Override
    public AccountAccessLog save(AccountAccessLog accountAccessLog) {
        AccountAccessLogJpaEntity jpaEntity = accountAccessLogMapper.toJpaEntity(accountAccessLog);
        AccountAccessLogJpaEntity savedEntity = accountAccessLogRepository.save(jpaEntity);
        return accountAccessLogMapper.toDomainEntity(savedEntity);
    }

    @Override
    public Page<AccountAccessLog> findAllByMemberId(String memberId, Pageable pageable) {
        return accountAccessLogRepository.findAllByMemberId(memberId, pageable)
                .map(accountAccessLogMapper::toDomainEntity);
    }
}
