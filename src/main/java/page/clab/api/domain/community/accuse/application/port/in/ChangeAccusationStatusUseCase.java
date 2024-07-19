package page.clab.api.domain.community.accuse.application.port.in;


import page.clab.api.domain.community.accuse.domain.AccuseStatus;
import page.clab.api.domain.community.accuse.domain.TargetType;

public interface ChangeAccusationStatusUseCase {
    Long changeAccusationStatus(TargetType type, Long targetId, AccuseStatus status);
}
