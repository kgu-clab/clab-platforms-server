package page.clab.api.domain.position.application.port.out;

import page.clab.api.domain.position.domain.Position;

public interface RegisterPositionPort {
    Position save(Position position);
}
