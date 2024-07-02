package page.clab.api.domain.accuse.application.port.out;

import page.clab.api.domain.accuse.domain.AccuseTarget;
import page.clab.api.domain.accuse.domain.AccuseTargetId;

import java.util.Optional;

public interface LoadAccuseTargetPort {
    Optional<AccuseTarget> findById(AccuseTargetId accuseTargetId);
    AccuseTarget findByIdOrThrow(AccuseTargetId accuseTargetId);
}
