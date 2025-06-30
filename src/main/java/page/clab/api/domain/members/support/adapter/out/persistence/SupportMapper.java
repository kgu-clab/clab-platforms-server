package page.clab.api.domain.members.support.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.members.support.domain.Support;

@Mapper(componentModel = "spring")
public interface SupportMapper {

    SupportJpaEntity toEntity(Support support);

    Support toDomain(SupportJpaEntity entity);
}