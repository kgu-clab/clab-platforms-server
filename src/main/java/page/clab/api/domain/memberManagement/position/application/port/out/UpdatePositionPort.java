package page.clab.api.domain.memberManagement.position.application.port.out;

import page.clab.api.domain.memberManagement.position.domain.Position;

public interface UpdatePositionPort {

    Position update(Position position);
}
