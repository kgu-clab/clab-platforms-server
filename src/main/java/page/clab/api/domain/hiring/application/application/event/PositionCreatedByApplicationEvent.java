package page.clab.api.domain.hiring.application.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import page.clab.api.domain.memberManagement.position.domain.Position;

@Getter
public class PositionCreatedByApplicationEvent extends ApplicationEvent {

    private final Position position;

    public PositionCreatedByApplicationEvent(Object source, Position position) {
        super(source);
        this.position = position;
    }
}
