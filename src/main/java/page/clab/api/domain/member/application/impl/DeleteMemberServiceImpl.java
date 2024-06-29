package page.clab.api.domain.member.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.DeleteMemberService;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dao.MemberRepository;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.event.MemberDeletedEvent;

@Service
@RequiredArgsConstructor
public class DeleteMemberServiceImpl implements DeleteMemberService {

    private final MemberLookupService memberLookupService;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public String deleteMember(String memberId) {
        Member member = memberLookupService.getMemberByIdOrThrow(memberId);
        memberRepository.delete(member);
        eventPublisher.publishEvent(new MemberDeletedEvent(this, member.getId()));
        return member.getId();
    }
}
