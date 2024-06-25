package page.clab.api.domain.member.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import page.clab.api.domain.member.domain.Member;

@Getter
public class MemberDeletedEvent extends ApplicationEvent {

    private final Member member;

    public MemberDeletedEvent(Object source, Member member) {
        super(source);
        this.member = member;
    }

}
