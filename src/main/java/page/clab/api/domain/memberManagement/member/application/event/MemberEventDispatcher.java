package page.clab.api.domain.memberManagement.member.application.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import page.clab.api.domain.hiring.application.application.event.ApplicationApprovedEvent;
import page.clab.api.domain.hiring.application.domain.Application;
import page.clab.api.domain.memberManagement.member.domain.Member;

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
}
