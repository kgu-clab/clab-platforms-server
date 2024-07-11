package page.clab.api.domain.community.board.application.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BoardEventDispatcher {

    private final List<BoardEventProcessor> processors;

    @EventListener
    public void handleBoardDeletedEvent(BoardDeletedEvent event) {
        processors.forEach(processor -> processor.processBoardDeleted(event.getBoardId()));
    }

    @EventListener
    public void handleBoardUpdatedEvent(BoardUpdatedEvent event) {
        processors.forEach(processor -> processor.processBoardUpdated(event.getBoardId()));
    }
}
