package page.clab.api.domain.memberManagement.member.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import page.clab.api.domain.hiring.application.domain.Application;

@Getter
public class MemberCreatedEvent extends ApplicationEvent {

    private final Application application;

    public MemberCreatedEvent(Object source, Application application) {
        super(source);
        this.application = application;
    }
}