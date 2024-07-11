package page.clab.api.domain.accuse.application.event;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.accuse.application.port.out.RegisterAccusePort;
import page.clab.api.domain.accuse.application.port.out.RetrieveAccusePort;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.member.application.event.MemberEventProcessor;
import page.clab.api.domain.member.application.event.MemberEventProcessorRegistry;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AccuseEventProcessor implements MemberEventProcessor {

    private final RetrieveAccusePort retrieveAccusePort;
    private final RegisterAccusePort registerAccusePort;
    private final MemberEventProcessorRegistry processorRegistry;

    @PostConstruct
    public void init() {
        processorRegistry.register(this);
    }

    @Override
    @Transactional
    public void processMemberDeleted(String memberId) {
        List<Accuse> accuses = retrieveAccusePort.findByMemberId(memberId);
        accuses.forEach(Accuse::delete);
        registerAccusePort.saveAll(accuses);
    }

    @Override
    public void processMemberUpdated(String memberId) {
        // do nothing
    }
}
