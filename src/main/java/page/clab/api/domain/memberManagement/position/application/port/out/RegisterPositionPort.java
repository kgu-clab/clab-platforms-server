package page.clab.api.domain.memberManagement.position.application.port.out;

import java.util.List;
import page.clab.api.domain.memberManagement.position.domain.Position;

public interface RegisterPositionPort {

    Position save(Position position);

    void saveAll(List<Position> positions);
}
