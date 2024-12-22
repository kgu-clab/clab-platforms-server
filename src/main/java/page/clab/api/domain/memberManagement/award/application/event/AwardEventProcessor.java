package page.clab.api.domain.memberManagement.award.application.event;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.award.application.port.out.RegisterAwardPort;
import page.clab.api.domain.memberManagement.award.application.port.out.RetrieveAwardPort;
import page.clab.api.domain.memberManagement.award.domain.Award;
import page.clab.api.domain.memberManagement.member.application.event.MemberEventProcessor;
import page.clab.api.domain.memberManagement.member.application.event.MemberEventProcessorRegistry;

@Component
@RequiredArgsConstructor
public class AwardEventProcessor implements MemberEventProcessor {

    private final RetrieveAwardPort retrieveAwardPort;
    private final RegisterAwardPort registerAwardPort;
    private final MemberEventProcessorRegistry processorRegistry;

    @PostConstruct
    public void init() {
        processorRegistry.register(this);
    }

    @Override
    @Transactional
    public void processMemberDeleted(String memberId) {
        List<Award> awards = retrieveAwardPort.findByMemberId(memberId);
        awards.forEach(Award::delete);
        registerAwardPort.saveAll(awards);
    }

    @Override
    public void processMemberUpdated(String memberId) {
        // do nothing
    }
}
