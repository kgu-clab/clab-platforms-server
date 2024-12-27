package page.clab.api.domain.memberManagement.executive.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.memberManagement.executive.domain.Executive;

@Mapper(componentModel = "spring")
public interface ExecutiveMapper {

    ExecutiveJpaEntity toEntity(Executive executive);

    Executive toDomain(ExecutiveJpaEntity jpaEntity);
}
