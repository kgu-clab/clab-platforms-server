package page.clab.api.domain.accuse.application.port.in;

import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.TargetType;

public interface ChangeAccusationStatusUseCase {
    Long changeAccusationStatus(TargetType type, Long targetId, AccuseStatus status);
}
