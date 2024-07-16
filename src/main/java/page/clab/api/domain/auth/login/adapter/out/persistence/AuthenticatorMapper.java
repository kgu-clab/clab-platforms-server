package page.clab.api.domain.auth.login.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.auth.login.domain.Authenticator;

@Mapper(componentModel = "spring")
public interface AuthenticatorMapper {

    AuthenticatorJpaEntity toJpaEntity(Authenticator authenticator);

    Authenticator toDomainEntity(AuthenticatorJpaEntity jpaEntity);
}
