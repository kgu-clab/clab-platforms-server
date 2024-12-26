package page.clab.api.domain.memberManagement.member.application.port.out;

import page.clab.api.domain.memberManagement.member.domain.Member;

public interface UpdateMemberPort {

    Member update(Member member);
}
