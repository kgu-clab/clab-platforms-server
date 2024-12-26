package page.clab.api.external.memberManagement.member.application.port;

import page.clab.api.domain.memberManagement.member.domain.Member;

public interface ExternalRegisterMemberUseCase {

    void save(Member member);
}
