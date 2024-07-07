package page.clab.api.domain.member.application.port.out;

import page.clab.api.domain.member.domain.Member;

public interface UpdateMemberPort {
    Member update(Member member);
}
