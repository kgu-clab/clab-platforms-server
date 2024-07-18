package page.clab.api.external.memberManagement.position.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.memberManagement.position.application.port.out.RegisterPositionPort;
import page.clab.api.domain.memberManagement.position.domain.Position;
import page.clab.api.external.memberManagement.position.application.port.ExternalRegisterPositionUseCase;

@Service
@RequiredArgsConstructor
public class ExternalPositionRegisterService implements ExternalRegisterPositionUseCase {

    private final RegisterPositionPort registerPositionPort;

    @Override
    public void save(Position position) {
        registerPositionPort.save(position);
    }
}
