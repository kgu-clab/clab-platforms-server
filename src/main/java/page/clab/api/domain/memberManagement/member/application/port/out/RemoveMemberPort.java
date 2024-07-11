package page.clab.api.domain.memberManagement.member.application.port.out;

import page.clab.api.domain.memberManagement.member.domain.Member;

public interface RemoveMemberPort {
    void delete(Member member);
}
