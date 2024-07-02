package page.clab.api.domain.position.application.port.out;

import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.domain.PositionType;

import java.util.Optional;

public interface RetrievePositionByMemberIdAndYearAndPositionTypePort {
    Optional<Position> findByMemberIdAndYearAndPositionType(String memberId, String year, PositionType positionType);
}
