package page.clab.api.domain.accuse.adapter.out.persistence;

import org.springframework.stereotype.Component;
import page.clab.api.domain.accuse.domain.AccuseTarget;

@Component
public class AccuseTargetMapper {

    public AccuseTargetJpaEntity toJpaEntity(AccuseTarget target) {
        return AccuseTargetJpaEntity.builder()
                .targetType(target.getTargetType())
                .targetReferenceId(target.getTargetReferenceId())
                .accuseCount(target.getAccuseCount())
                .accuseStatus(target.getAccuseStatus())
                .build();
    }

    public AccuseTarget toDomain(AccuseTargetJpaEntity entity) {
        return AccuseTarget.builder()
                .targetType(entity.getTargetType())
                .targetReferenceId(entity.getTargetReferenceId())
                .accuseCount(entity.getAccuseCount())
                .accuseStatus(entity.getAccuseStatus())
                .build();
    }
}
