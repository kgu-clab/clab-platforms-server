package page.clab.api.external.memberManagement.position.application.port;

import page.clab.api.domain.memberManagement.position.domain.Position;

public interface ExternalRegisterPositionUseCase {

    void save(Position position);
}
