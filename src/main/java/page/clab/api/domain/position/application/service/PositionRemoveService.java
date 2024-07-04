package page.clab.api.domain.position.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.position.application.port.in.RemovePositionUseCase;
import page.clab.api.domain.position.application.port.out.RetrievePositionPort;
import page.clab.api.domain.position.application.port.out.UpdatePositionPort;
import page.clab.api.domain.position.domain.Position;

@Service
@RequiredArgsConstructor
public class PositionRemoveService implements RemovePositionUseCase {

    private final RetrievePositionPort retrievePositionPort;
    private final UpdatePositionPort updatePositionPort;

    @Transactional
    public Long remove(Long positionId) {
        Position position = retrievePositionPort.findByIdOrThrow(positionId);
        position.delete();
        return updatePositionPort.update(position).getId();
    }
}
