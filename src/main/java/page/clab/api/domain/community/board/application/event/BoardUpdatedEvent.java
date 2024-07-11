package page.clab.api.domain.community.board.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BoardUpdatedEvent extends ApplicationEvent {

    private final Long boardId;

    public BoardUpdatedEvent(Object source, Long boardId) {
        super(source);
        this.boardId = boardId;
    }
}
