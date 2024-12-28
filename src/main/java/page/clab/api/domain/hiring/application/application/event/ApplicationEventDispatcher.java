package page.clab.api.domain.hiring.application.application.event;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationEventDispatcher {

    private final List<ApplicationEventProcessor> processors;

    @EventListener
    public void handleApplicationMemberCreatedEvent(ApplicationMemberCreatedEvent event) {
        processors.forEach(processor -> processor.processApplicationMemberCreated(event.getDto()));
    }

    @EventListener
    public void handlePositionCreatedByApplicationEvent(PositionCreatedByApplicationEvent event) {
        processors.forEach(processor -> processor.processPositionCreated(event.getMemberId()));
    }
}
