package page.clab.api.domain.member.application.port.in;

import page.clab.api.domain.member.domain.Member;

import java.util.Optional;

public interface MemberRetrievalUseCase {
    Optional<Member> findById(String memberId);
    Member findByIdOrThrow(String memberId);
    Member findByEmail(String email);
    Member getCurrentMember();
    String getCurrentMemberId();
}
