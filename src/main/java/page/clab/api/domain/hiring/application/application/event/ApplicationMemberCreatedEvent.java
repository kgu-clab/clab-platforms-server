package page.clab.api.domain.hiring.application.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import page.clab.api.domain.hiring.application.application.dto.request.ApplicationMemberCreationDto;

@Getter
public class ApplicationMemberCreatedEvent extends ApplicationEvent {

    private final ApplicationMemberCreationDto dto;

    public ApplicationMemberCreatedEvent(Object source, ApplicationMemberCreationDto dto) {
        super(source);
        this.dto = dto;
    }
}
