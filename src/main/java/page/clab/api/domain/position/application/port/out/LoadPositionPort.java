package page.clab.api.domain.position.application.port.out;

import page.clab.api.domain.position.domain.Position;

import java.util.Optional;

public interface LoadPositionPort {
    Optional<Position> findById(Long id);
    Position findByIdOrThrow(Long id);
}
