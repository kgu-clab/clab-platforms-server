package page.clab.api.domain.accuse.application.port.out;

import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.TargetType;

import java.util.List;

public interface RetrieveAccuseByTargetPort {
    List<Accuse> findByTargetOrderByCreatedAtDesc(TargetType targetType, Long targetReferenceId);
    List<Accuse> findByTarget(TargetType targetType, Long targetReferenceId);
}
