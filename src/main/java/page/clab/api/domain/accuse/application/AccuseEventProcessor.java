package page.clab.api.domain.accuse.application;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.accuse.dao.AccuseRepository;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.member.event.MemberEventProcessor;
import page.clab.api.domain.member.event.MemberEventProcessorRegistry;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AccuseEventProcessor implements MemberEventProcessor {

    private final AccuseRepository accuseRepository;

    private final MemberEventProcessorRegistry processorRegistry;

    @PostConstruct
    public void init() {
        processorRegistry.registerProcessor(this);
    }

    @Override
    @Transactional
    public void processMemberDeleted(String memberId) {
        List<Accuse> accuses = accuseRepository.findByMemberId(memberId);
        accuses.forEach(Accuse::delete);
        accuseRepository.saveAll(accuses);
    }

    @Override
    public void processMemberUpdated(String memberId) {
        // do nothing
    }

}
