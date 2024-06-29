package page.clab.api.domain.accuse.application;

import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.TargetType;

public interface ChangeAccusationStatusService {
    Long changeAccusationStatus(TargetType type, Long targetId, AccuseStatus status);
}
