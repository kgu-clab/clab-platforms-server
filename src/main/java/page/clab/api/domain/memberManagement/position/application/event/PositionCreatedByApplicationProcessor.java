package page.clab.api.domain.memberManagement.position.application.event;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.hiring.application.application.event.ApplicationEventProcessor;
import page.clab.api.domain.hiring.application.application.event.ApplicationEventProcessorRegistry;
import page.clab.api.domain.hiring.application.domain.Application;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.domain.memberManagement.position.application.port.out.RegisterPositionPort;
import page.clab.api.domain.memberManagement.position.domain.Position;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;

@Component
@RequiredArgsConstructor
public class PositionCreatedByApplicationProcessor implements ApplicationEventProcessor {

    private final RegisterPositionPort registerPositionPort;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ApplicationEventProcessorRegistry processorRegistry;

    @PostConstruct
    public void init() {
        processorRegistry.register(this);
    }

    @Override
    public void processApplicationMemberCreated(Application application) {
        // do nothing
    }

    @Override
    public void processPositionCreated(String memberId) {
        Member member = externalRetrieveMemberUseCase.findByIdOrThrow(memberId);
        Position position = Position.create(member.getId());
        registerPositionPort.save(position);
    }
}
