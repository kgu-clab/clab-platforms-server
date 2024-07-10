package page.clab.api.domain.member.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MemberUpdatedEvent extends ApplicationEvent {

    private final String memberId;

    public MemberUpdatedEvent(Object source, String memberId) {
        super(source);
        this.memberId = memberId;
    }
}
