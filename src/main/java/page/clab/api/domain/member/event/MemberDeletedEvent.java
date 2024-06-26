package page.clab.api.domain.member.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MemberDeletedEvent extends ApplicationEvent {

    private final String memberId;

    public MemberDeletedEvent(Object source, String memberId) {
        super(source);
        this.memberId = memberId;
    }

}
