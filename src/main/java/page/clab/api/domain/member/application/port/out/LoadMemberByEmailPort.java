package page.clab.api.domain.member.application.port.out;

import page.clab.api.domain.member.domain.Member;

import java.util.Optional;

public interface LoadMemberByEmailPort {
    Optional<Member> findByEmail(String email);
    Member findByEmailOrThrow(String email);
}