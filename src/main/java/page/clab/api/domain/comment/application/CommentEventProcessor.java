package page.clab.api.domain.comment.application;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.event.MemberEventProcessor;
import page.clab.api.domain.member.event.MemberEventProcessorRegistry;

@Component
@RequiredArgsConstructor
public class CommentEventProcessor implements MemberEventProcessor {

    private final MemberEventProcessorRegistry processorRegistry;

    @PostConstruct
    public void init() {
        processorRegistry.registerProcessor(this);
    }

    @Override
    @Transactional
    public void processMemberDeleted(Member member) {
        // do nothing
    }

    @Override
    @Transactional
    public void processMemberUpdated(Member member) {
        // do nothing
    }

}
