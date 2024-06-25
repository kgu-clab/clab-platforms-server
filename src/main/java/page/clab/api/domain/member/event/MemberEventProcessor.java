package page.clab.api.domain.member.event;

import page.clab.api.domain.member.domain.Member;

public interface MemberEventProcessor {

    void processMemberDeleted(Member member);

    void processMemberUpdated(Member member);

}
