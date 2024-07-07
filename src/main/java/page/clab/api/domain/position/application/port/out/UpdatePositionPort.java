package page.clab.api.domain.position.application.port.out;

import page.clab.api.domain.position.domain.Position;

public interface UpdatePositionPort {
    Position update(Position position);
}
