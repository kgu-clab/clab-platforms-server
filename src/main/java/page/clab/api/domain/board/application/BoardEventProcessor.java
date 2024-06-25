package page.clab.api.domain.board.application;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.dao.BoardRepository;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.event.MemberEventProcessor;
import page.clab.api.domain.member.event.MemberEventProcessorRegistry;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BoardEventProcessor implements MemberEventProcessor {

    private final BoardRepository boardRepository;

    private final MemberEventProcessorRegistry processorRegistry;

    @PostConstruct
    public void init() {
        processorRegistry.registerProcessor(this);
    }

    @Override
    @Transactional
    public void processMemberDeleted(Member member) {
        List<Board> boards = boardRepository.findByMemberId(member.getId());
        boards.forEach(Board::delete);
        boardRepository.saveAll(boards);
    }

    @Override
    public void processMemberUpdated(Member member) {
        // do nothing
    }
}
