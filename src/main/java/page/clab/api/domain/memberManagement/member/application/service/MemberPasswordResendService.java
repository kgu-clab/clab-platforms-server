package page.clab.api.domain.memberManagement.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.port.in.ResendMemberPasswordUseCase;
import page.clab.api.domain.memberManagement.member.application.port.out.RegisterMemberPort;
import page.clab.api.domain.memberManagement.member.application.port.out.RetrieveMemberPort;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.global.common.email.application.EmailService;
import page.clab.api.global.common.verification.application.VerificationService;

@Service
@RequiredArgsConstructor
public class MemberPasswordResendService implements ResendMemberPasswordUseCase {

    private final RegisterMemberPort registerMemberPort;
    private final RetrieveMemberPort retrieveMemberPort;
    private final VerificationService verificationService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public String resendMemberPassword(String memberId) {
        Member member = retrieveMemberPort.findByIdOrThrow(memberId);

        String newPassword = verificationService.generateVerificationCode();
        member.updatePassword(newPassword, passwordEncoder);
        registerMemberPort.save(member);

        emailService.broadcastEmailToApprovedMember(member, newPassword);
        return member.getId();
    }
}
