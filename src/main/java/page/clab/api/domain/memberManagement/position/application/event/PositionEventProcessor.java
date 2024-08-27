package page.clab.api.domain.memberManagement.position.application.event;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.hiring.application.domain.Application;
import page.clab.api.domain.memberManagement.member.application.event.MemberEventProcessor;
import page.clab.api.domain.memberManagement.member.application.event.MemberEventProcessorRegistry;
import page.clab.api.domain.memberManagement.position.application.port.out.RegisterPositionPort;
import page.clab.api.domain.memberManagement.position.application.port.out.RetrievePositionPort;
import page.clab.api.domain.memberManagement.position.domain.Position;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PositionEventProcessor implements MemberEventProcessor {

    private final RetrievePositionPort retrievePositionPort;
    private final RegisterPositionPort registerPositionPort;
    private final MemberEventProcessorRegistry processorRegistry;

    @PostConstruct
    public void init() {
        processorRegistry.register(this);
    }

    @Override
    @Transactional
    public void processMemberDeleted(String memberId) {
        List<Position> positions = retrievePositionPort.findByMemberId(memberId);
        positions.forEach(Position::delete);
        registerPositionPort.saveAll(positions);
    }

    @Override
    public void processMemberUpdated(String memberId) {
        // do nothing
    }

    @Override
    public void processMemberCreated(Application application) {
        // do nothing
    }
}
