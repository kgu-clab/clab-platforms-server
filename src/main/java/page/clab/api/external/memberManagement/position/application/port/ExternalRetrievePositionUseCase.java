package page.clab.api.external.memberManagement.position.application.port;

import java.util.Optional;
import page.clab.api.domain.memberManagement.position.domain.Position;
import page.clab.api.domain.memberManagement.position.domain.PositionType;

public interface ExternalRetrievePositionUseCase {

    Optional<Position> findByMemberIdAndYearAndPositionType(String memberId, String year, PositionType positionType);
}
