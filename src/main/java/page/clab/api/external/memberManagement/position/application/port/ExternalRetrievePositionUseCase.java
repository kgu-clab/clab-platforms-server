package page.clab.api.external.memberManagement.position.application.port;

import page.clab.api.domain.memberManagement.position.domain.Position;
import page.clab.api.domain.memberManagement.position.domain.PositionType;

import java.util.Optional;

public interface ExternalRetrievePositionUseCase {
    Optional<Position> findByMemberIdAndYearAndPositionType(String memberId, String year, PositionType positionType);
}
