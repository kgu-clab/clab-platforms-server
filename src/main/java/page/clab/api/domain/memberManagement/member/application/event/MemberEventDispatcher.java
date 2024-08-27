package page.clab.api.domain.memberManagement.member.application.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberEventDispatcher {

    private final List<MemberEventProcessor> processors;

    @EventListener
    public void handleMemberDeletedEvent(MemberDeletedEvent event) {
        processors.forEach(processor -> processor.processMemberDeleted(event.getMemberId()));
    }

    @EventListener
    public void handleMemberUpdatedEvent(MemberUpdatedEvent event) {
        processors.forEach(processor -> processor.processMemberUpdated(event.getMemberId()));
    }

    @EventListener
    public void handleMemberCreatedEvent(MemberCreatedEvent event) {
        processors.forEach(processor -> processor.processMemberCreated(event.getApplication()));
    }
}
