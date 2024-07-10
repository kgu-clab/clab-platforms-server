package page.clab.api.domain.login.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.login.domain.Authenticator;

@Component
public class AuthenticatorMapper {

    public AuthenticatorJpaEntity toJpaEntity(Authenticator authenticator) {
        return AuthenticatorJpaEntity.builder()
                .memberId(authenticator.getMemberId())
                .secretKey(authenticator.getSecretKey())
                .build();
    }

    public Authenticator toDomainEntity(AuthenticatorJpaEntity jpaEntity) {
        return Authenticator.builder()
                .memberId(jpaEntity.getMemberId())
                .secretKey(jpaEntity.getSecretKey())
                .build();
    }
}
