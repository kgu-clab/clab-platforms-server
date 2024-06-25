package page.clab.api.domain.member.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import page.clab.api.domain.member.domain.Member;

@Getter
public class MemberUpdatedEvent extends ApplicationEvent {

    private final Member member;

    public MemberUpdatedEvent(Object source, Member member) {
        super(source);
        this.member = member;
    }

}
