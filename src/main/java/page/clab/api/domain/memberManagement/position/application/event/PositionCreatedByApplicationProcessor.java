package page.clab.api.domain.memberManagement.position.application.event;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.hiring.application.application.dto.request.ApplicationMemberCreationDto;
import page.clab.api.domain.hiring.application.application.event.ApplicationEventProcessor;
import page.clab.api.domain.hiring.application.application.event.ApplicationEventProcessorRegistry;
import page.clab.api.domain.memberManagement.position.application.port.out.RegisterPositionPort;
import page.clab.api.domain.memberManagement.position.domain.Position;

@Component
@RequiredArgsConstructor
public class PositionCreatedByApplicationProcessor implements ApplicationEventProcessor {

    private final RegisterPositionPort registerPositionPort;
    private final ApplicationEventProcessorRegistry processorRegistry;

    @PostConstruct
    public void init() {
        processorRegistry.register(this);
    }

    @Override
    public void processApplicationMemberCreated(ApplicationMemberCreationDto dto) {
        // do nothing
    }

    @Override
    public void processPositionCreated(String memberId) {
        Position position = Position.create(memberId);
        registerPositionPort.save(position);
    }
}
