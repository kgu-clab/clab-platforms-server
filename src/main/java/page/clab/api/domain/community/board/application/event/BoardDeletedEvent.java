package page.clab.api.domain.community.board.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BoardDeletedEvent extends ApplicationEvent {

    private final Long boardId;

    public BoardDeletedEvent(Object source, Long boardId) {
        super(source);
        this.boardId = boardId;
    }
}
