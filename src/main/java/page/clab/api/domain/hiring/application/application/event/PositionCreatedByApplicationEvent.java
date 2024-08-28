package page.clab.api.domain.hiring.application.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PositionCreatedByApplicationEvent extends ApplicationEvent {

    private final String memberId;

    public PositionCreatedByApplicationEvent(Object source, String memberId) {
        super(source);
        this.memberId = memberId;
    }
}
