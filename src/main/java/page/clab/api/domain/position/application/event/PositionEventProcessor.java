package page.clab.api.domain.position.application.event;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.event.MemberEventProcessor;
import page.clab.api.domain.member.application.event.MemberEventProcessorRegistry;
import page.clab.api.domain.position.adapter.out.persistence.PositionRepository;
import page.clab.api.domain.position.domain.Position;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PositionEventProcessor implements MemberEventProcessor {

    private final PositionRepository positionRepository;

    private final MemberEventProcessorRegistry processorRegistry;

    @PostConstruct
    public void init() {
        processorRegistry.register(this);
    }

    @Override
    @Transactional
    public void processMemberDeleted(String memberId) {
        List<Position> positions = positionRepository.findByMemberId(memberId);
        positions.forEach(Position::delete);
        positionRepository.saveAll(positions);
    }

    @Override
    public void processMemberUpdated(String memberId) {
        // do nothing
    }
}
