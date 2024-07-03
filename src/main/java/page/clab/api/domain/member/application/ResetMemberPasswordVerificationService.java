package page.clab.api.domain.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.VerifyResetMemberPasswordUseCase;
import page.clab.api.domain.member.application.port.out.LoadMemberPort;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.verification.application.VerificationService;
import page.clab.api.global.common.verification.domain.Verification;
import page.clab.api.global.common.verification.dto.request.VerificationRequestDto;

@Service
@RequiredArgsConstructor
public class ResetMemberPasswordVerificationService implements VerifyResetMemberPasswordUseCase {

    private final LoadMemberPort loadMemberPort;
    private final VerificationService verificationService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public String verify(VerificationRequestDto requestDto) {
        Member member = loadMemberPort.findByIdOrThrow(requestDto.getMemberId());
        Verification verification = verificationService.validateVerificationCode(requestDto, member);
        updateMemberPasswordWithVerificationCode(verification.getVerificationCode(), member);
        return member.getId();
    }

    private void updateMemberPasswordWithVerificationCode(String verificationCode, Member member) {
        member.updatePassword(verificationCode, passwordEncoder);
        verificationService.deleteVerificationCode(verificationCode);
    }
}
