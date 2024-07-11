package page.clab.api.domain.auth.accountLockInfo.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.auth.accountLockInfo.domain.AccountLockInfo;

@Component
public class AccountLockInfoMapper {

    public AccountLockInfoJpaEntity toJpaEntity(AccountLockInfo accountLockInfo) {
        return AccountLockInfoJpaEntity.builder()
                .id(accountLockInfo.getId())
                .memberId(accountLockInfo.getMemberId())
                .loginFailCount(accountLockInfo.getLoginFailCount())
                .isLock(accountLockInfo.getIsLock())
                .lockUntil(accountLockInfo.getLockUntil())
                .build();
    }

    public AccountLockInfo toDomainEntity(AccountLockInfoJpaEntity jpaEntity) {
        return AccountLockInfo.builder()
                .id(jpaEntity.getId())
                .memberId(jpaEntity.getMemberId())
                .loginFailCount(jpaEntity.getLoginFailCount())
                .isLock(jpaEntity.getIsLock())
                .lockUntil(jpaEntity.getLockUntil())
                .build();
    }
}
