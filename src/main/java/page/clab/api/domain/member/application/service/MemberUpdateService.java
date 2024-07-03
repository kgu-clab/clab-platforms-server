package page.clab.api.domain.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.member.application.port.in.UpdateMemberUseCase;
import page.clab.api.domain.member.application.port.out.LoadMemberPort;
import page.clab.api.domain.member.application.port.out.RegisterMemberPort;
import page.clab.api.domain.member.domain.Member;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberUpdateService implements UpdateMemberUseCase {

    private final LoadMemberPort loadMemberPort;
    private final RegisterMemberPort registerMemberPort;

    @Override
    public void updateLoanSuspensionDate(String memberId, LocalDateTime loanSuspensionDate) {
        Member member = loadMemberPort.findByIdOrThrow(memberId);
        member.updateLoanSuspensionDate(loanSuspensionDate);
        registerMemberPort.save(member);
    }

    @Override
    public void updateLastLoginTime(String memberId) {
        Member member = loadMemberPort.findByIdOrThrow(memberId);
        member.updateLastLoginTime();
        registerMemberPort.save(member);
    }
}
