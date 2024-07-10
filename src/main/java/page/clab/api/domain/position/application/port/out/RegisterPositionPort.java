package page.clab.api.domain.position.application.port.out;

import page.clab.api.domain.position.domain.Position;

import java.util.List;

public interface RegisterPositionPort {
    Position save(Position position);

    void saveAll(List<Position> positions);
}
