package page.clab.api.domain.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.RemoveMemberUseCase;
import page.clab.api.domain.member.application.port.out.LoadMemberPort;
import page.clab.api.domain.member.application.port.out.RegisterMemberPort;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.event.MemberDeletedEvent;

@Service
@RequiredArgsConstructor
public class MemberRemoveService implements RemoveMemberUseCase {

    private final LoadMemberPort loadMemberPort;
    private final RegisterMemberPort registerMemberPort;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public String remove(String memberId) {
        Member member = loadMemberPort.findByIdOrThrow(memberId);
        member.delete();
        registerMemberPort.save(member);
        eventPublisher.publishEvent(new MemberDeletedEvent(this, member.getId()));
        return member.getId();
    }
}
