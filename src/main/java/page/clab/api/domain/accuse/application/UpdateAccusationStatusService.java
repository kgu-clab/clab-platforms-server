package page.clab.api.domain.accuse.application;

import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.TargetType;

public interface UpdateAccusationStatusService {
    Long execute(TargetType type, Long targetId, AccuseStatus status);
}
