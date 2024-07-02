package page.clab.api.domain.position.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.position.domain.Position;

public interface RetrieveDeletedPositionsPort {
    Page<Position> findAllByIsDeletedTrue(Pageable pageable);
}
