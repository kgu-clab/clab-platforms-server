package page.clab.api.domain.board.application.event;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.port.out.RegisterBoardPort;
import page.clab.api.domain.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.member.application.event.MemberEventProcessor;
import page.clab.api.domain.member.application.event.MemberEventProcessorRegistry;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BoardEventProcessor implements MemberEventProcessor {

    private final RetrieveBoardPort retrieveBoardPort;
    private final RegisterBoardPort registerBoardPort;
    private final MemberEventProcessorRegistry processorRegistry;

    @PostConstruct
    public void init() {
        processorRegistry.register(this);
    }

    @Override
    @Transactional
    public void processMemberDeleted(String memberId) {
        List<Board> boards = retrieveBoardPort.findByMemberId(memberId);
        boards.forEach(Board::delete);
        registerBoardPort.saveAll(boards);
    }

    @Override
    public void processMemberUpdated(String memberId) {
        // do nothing
    }
}
