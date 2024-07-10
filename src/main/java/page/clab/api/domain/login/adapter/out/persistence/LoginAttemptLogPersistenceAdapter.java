package page.clab.api.domain.login.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.login.application.port.out.RegisterLoginAttemptLogPort;
import page.clab.api.domain.login.application.port.out.RetrieveLoginAttemptLogPort;
import page.clab.api.domain.login.domain.LoginAttemptLog;

@Component
@RequiredArgsConstructor
public class LoginAttemptLogPersistenceAdapter implements
        RegisterLoginAttemptLogPort,
        RetrieveLoginAttemptLogPort {

    private final LoginAttemptLogRepository loginAttemptLogRepository;
    private final LoginAttemptLogMapper loginAttemptLogMapper;

    @Override
    public LoginAttemptLog save(LoginAttemptLog loginAttemptLog) {
        LoginAttemptLogJpaEntity jpaEntity = loginAttemptLogMapper.toJpaEntity(loginAttemptLog);
        LoginAttemptLogJpaEntity savedEntity = loginAttemptLogRepository.save(jpaEntity);
        return loginAttemptLogMapper.toDomainEntity(savedEntity);
    }

    @Override
    public Page<LoginAttemptLog> findAllByMemberId(String memberId, Pageable pageable) {
        return loginAttemptLogRepository.findAllByMemberId(memberId, pageable)
                .map(loginAttemptLogMapper::toDomainEntity);
    }
}
