package page.clab.api.domain.position.application.port.out;

import page.clab.api.domain.position.domain.Position;

import java.util.List;

public interface RetrieveAllPositionsByMemberIdAndYearPort {
    List<Position> findAllByMemberIdAndYearOrderByPositionTypeAsc(String memberId, String year);
}
