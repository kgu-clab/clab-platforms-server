package page.clab.api.domain.memberManagement.position.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.memberManagement.position.domain.Position;

@Component
public class PositionMapper {

    public PositionJpaEntity toJpaEntity(Position position) {
        return PositionJpaEntity.builder()
                .id(position.getId())
                .memberId(position.getMemberId())
                .positionType(position.getPositionType())
                .year(position.getYear())
                .isDeleted(position.isDeleted())
                .build();
    }

    public Position toDomainEntity(PositionJpaEntity jpaEntity) {
        return Position.builder()
                .id(jpaEntity.getId())
                .memberId(jpaEntity.getMemberId())
                .positionType(jpaEntity.getPositionType())
                .year(jpaEntity.getYear())
                .isDeleted(jpaEntity.isDeleted())
                .build();
    }
}
