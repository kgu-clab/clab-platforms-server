package page.clab.api.domain.position.application;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.event.MemberEventProcessor;
import page.clab.api.domain.member.event.MemberEventProcessorRegistry;
import page.clab.api.domain.position.dao.PositionRepository;
import page.clab.api.domain.position.domain.Position;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PositionEventProcessor implements MemberEventProcessor {

    private final PositionRepository positionRepository;

    private final MemberEventProcessorRegistry processorRegistry;

    @PostConstruct
    public void init() {
        processorRegistry.registerProcessor(this);
    }

    @Override
    @Transactional
    public void processMemberDeleted(Member member) {
        List<Position> positions = positionRepository.findByMemberId(member.getId());
        positions.forEach(Position::delete);
        positionRepository.saveAll(positions);
    }

    @Override
    public void processMemberUpdated(Member member) {
        // do nothing
    }

}
