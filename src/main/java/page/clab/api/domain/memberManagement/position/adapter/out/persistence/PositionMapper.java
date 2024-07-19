package page.clab.api.domain.memberManagement.position.adapter.out.persistence;

import org.mapstruct.Mapper;
import page.clab.api.domain.memberManagement.position.domain.Position;

@Mapper(componentModel = "spring")
public interface PositionMapper {

    PositionJpaEntity toJpaEntity(Position position);

    Position toDomainEntity(PositionJpaEntity jpaEntity);
}
