package page.clab.api.external.memberManagement.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.memberManagement.member.application.port.out.RegisterMemberPort;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.external.memberManagement.member.application.port.ExternalRegisterMemberUseCase;

@Service
@RequiredArgsConstructor
public class ExternalMemberRegisterService implements ExternalRegisterMemberUseCase {

    private final RegisterMemberPort registerMemberPort;

    @Override
    public void save(Member member) {
        registerMemberPort.save(member);
    }
}
