package page.clab.api.domain.position.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.position.application.port.in.PositionRemoveUseCase;
import page.clab.api.domain.position.application.port.out.LoadPositionPort;
import page.clab.api.domain.position.application.port.out.UpdatePositionPort;
import page.clab.api.domain.position.domain.Position;

@Service
@RequiredArgsConstructor
public class PositionRemoveService implements PositionRemoveUseCase {

    private final LoadPositionPort loadPositionPort;
    private final UpdatePositionPort updatePositionPort;

    @Transactional
    public Long remove(Long positionId) {
        Position position = loadPositionPort.findByIdOrThrow(positionId);
        position.delete();
        return updatePositionPort.update(position).getId();
    }
}
