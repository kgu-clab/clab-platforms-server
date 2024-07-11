package page.clab.api.domain.login.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.login.domain.AccountAccessLog;

@Component
public class AccountAccessLogMapper {

    public AccountAccessLogJpaEntity toJpaEntity(AccountAccessLog accountAccessLog) {
        return AccountAccessLogJpaEntity.builder()
                .id(accountAccessLog.getId())
                .memberId(accountAccessLog.getMemberId())
                .userAgent(accountAccessLog.getUserAgent())
                .ipAddress(accountAccessLog.getIpAddress())
                .location(accountAccessLog.getLocation())
                .accountAccessResult(accountAccessLog.getAccountAccessResult())
                .accessTime(accountAccessLog.getAccessTime())
                .build();
    }

    public AccountAccessLog toDomainEntity(AccountAccessLogJpaEntity jpaEntity) {
        return AccountAccessLog.builder()
                .id(jpaEntity.getId())
                .memberId(jpaEntity.getMemberId())
                .userAgent(jpaEntity.getUserAgent())
                .ipAddress(jpaEntity.getIpAddress())
                .location(jpaEntity.getLocation())
                .accountAccessResult(jpaEntity.getAccountAccessResult())
                .accessTime(jpaEntity.getAccessTime())
                .build();
    }
}
