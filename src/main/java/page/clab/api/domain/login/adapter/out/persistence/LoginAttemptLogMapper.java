package page.clab.api.domain.login.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.login.domain.LoginAttemptLog;

@Component
public class LoginAttemptLogMapper {

    public LoginAttemptLogJpaEntity toJpaEntity(LoginAttemptLog loginAttemptLog) {
        return LoginAttemptLogJpaEntity.builder()
                .id(loginAttemptLog.getId())
                .memberId(loginAttemptLog.getMemberId())
                .userAgent(loginAttemptLog.getUserAgent())
                .ipAddress(loginAttemptLog.getIpAddress())
                .location(loginAttemptLog.getLocation())
                .loginAttemptResult(loginAttemptLog.getLoginAttemptResult())
                .loginAttemptTime(loginAttemptLog.getLoginAttemptTime())
                .build();
    }

    public LoginAttemptLog toDomainEntity(LoginAttemptLogJpaEntity jpaEntity) {
        return LoginAttemptLog.builder()
                .id(jpaEntity.getId())
                .memberId(jpaEntity.getMemberId())
                .userAgent(jpaEntity.getUserAgent())
                .ipAddress(jpaEntity.getIpAddress())
                .location(jpaEntity.getLocation())
                .loginAttemptResult(jpaEntity.getLoginAttemptResult())
                .loginAttemptTime(jpaEntity.getLoginAttemptTime())
                .build();
    }
}
