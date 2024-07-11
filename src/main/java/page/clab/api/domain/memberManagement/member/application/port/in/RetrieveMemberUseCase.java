package page.clab.api.domain.memberManagement.member.application.port.in;

import page.clab.api.domain.memberManagement.member.domain.Member;

import java.util.Optional;

public interface RetrieveMemberUseCase {
    Optional<Member> findById(String memberId);

    Member findByIdOrThrow(String memberId);

    Member findByEmail(String email);

    Member getCurrentMember();

    String getCurrentMemberId();
}
