package page.clab.api.domain.position.application.port.out;

import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.domain.PositionType;

import java.util.List;
import java.util.Optional;

public interface LoadPositionPort {
    Optional<Position> findById(Long id);
    Position findByIdOrThrow(Long id);
    Optional<Position> findByMemberIdAndYearAndPositionType(String memberId, String year, PositionType positionType);
    List<Position> findAllByMemberIdAndYearOrderByPositionTypeAsc(String memberId, String year);
}
