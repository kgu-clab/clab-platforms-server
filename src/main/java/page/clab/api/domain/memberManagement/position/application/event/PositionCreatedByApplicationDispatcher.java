package page.clab.api.domain.memberManagement.position.application.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.hiring.application.application.event.PositionCreatedByApplicationEvent;
import page.clab.api.domain.memberManagement.position.application.port.out.RegisterPositionPort;
import page.clab.api.domain.memberManagement.position.domain.Position;

@Component
@RequiredArgsConstructor
public class PositionCreatedByApplicationDispatcher {

    private final RegisterPositionPort registerPositionPort;

    @EventListener
    @Transactional
    public void handlePositionCreatedEvent(PositionCreatedByApplicationEvent event) {
        Position position = event.getPosition();
        registerPositionPort.save(position);
    }
}
