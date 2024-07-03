package page.clab.api.domain.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.member.application.port.out.LoadMemberByEmailPort;
import page.clab.api.domain.member.application.port.out.LoadMemberPort;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.auth.util.AuthUtil;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberRetrievalService implements RetrieveMemberUseCase {

    private final LoadMemberPort loadMemberPort;
    private final LoadMemberByEmailPort loadMemberByEmailPort;

    @Override
    public Optional<Member> findById(String memberId) {
        return loadMemberPort.findById(memberId);
    }

    @Override
    public Member findByIdOrThrow(String memberId) {
        return loadMemberPort.findByIdOrThrow(memberId);
    }

    @Override
    public Member findByEmail(String email) {
        return loadMemberByEmailPort.findByEmailOrThrow(email);
    }

    @Override
    public Member getCurrentMember() {
        String memberId = AuthUtil.getAuthenticationInfoMemberId();
        return loadMemberPort.findByIdOrThrow(memberId);
    }

    @Override
    public String getCurrentMemberId() {
        return AuthUtil.getAuthenticationInfoMemberId();
    }
}
