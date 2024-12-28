package page.clab.api.domain.memberManagement.member.application.port.out;

import page.clab.api.domain.memberManagement.member.domain.Member;

public interface RegisterMemberPort {

    Member save(Member member);
}
