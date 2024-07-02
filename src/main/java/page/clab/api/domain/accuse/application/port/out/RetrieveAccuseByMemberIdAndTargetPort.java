package page.clab.api.domain.accuse.application.port.out;

import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.TargetType;

import java.util.Optional;

public interface RetrieveAccuseByMemberIdAndTargetPort {
    Optional<Accuse> findByMemberIdAndTarget(String memberId, TargetType targetType, Long targetReferenceId);
}
