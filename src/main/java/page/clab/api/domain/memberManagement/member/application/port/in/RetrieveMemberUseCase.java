package page.clab.api.domain.memberManagement.member.application.port.in;

import page.clab.api.domain.memberManagement.member.domain.Member;

public interface RetrieveMemberUseCase {

    Member getCurrentMember();
}
