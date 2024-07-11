package page.clab.api.domain.accuse.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.accuse.domain.Accuse;

@Component
@RequiredArgsConstructor
public class AccuseMapper {

    private final AccuseTargetMapper accuseTargetMapper;

    public AccuseJpaEntity toJpaEntity(Accuse accuse) {
        return AccuseJpaEntity.builder()
                .id(accuse.getId())
                .memberId(accuse.getMemberId())
                .target(accuseTargetMapper.toJpaEntity(accuse.getTarget()))
                .reason(accuse.getReason())
                .isDeleted(accuse.isDeleted())
                .build();
    }

    public Accuse toDomain(AccuseJpaEntity entity) {
        return Accuse.builder()
                .id(entity.getId())
                .memberId(entity.getMemberId())
                .target(accuseTargetMapper.toDomain(entity.getTarget()))
                .reason(entity.getReason())
                .isDeleted(entity.isDeleted())
                .build();
    }
}
