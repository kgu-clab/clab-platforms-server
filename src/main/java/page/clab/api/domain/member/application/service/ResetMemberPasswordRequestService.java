package page.clab.api.domain.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.RequestResetMemberPasswordUseCase;
import page.clab.api.domain.member.application.port.out.RetrieveMemberPort;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.request.MemberResetPasswordRequestDto;
import page.clab.api.global.common.email.application.EmailService;
import page.clab.api.global.common.verification.application.VerificationService;
import page.clab.api.global.exception.InvalidInformationException;

@Service
@RequiredArgsConstructor
public class ResetMemberPasswordRequestService implements RequestResetMemberPasswordUseCase {

    private final RetrieveMemberPort retrieveMemberPort;
    private final VerificationService verificationService;
    private final EmailService emailService;

    @Transactional
    @Override
    public String requestResetMemberPassword(MemberResetPasswordRequestDto requestDto) {
        Member member = validateResetPasswordRequest(requestDto);
        String code = verificationService.generateVerificationCode();
        verificationService.saveVerificationCode(member.getId(), code);
        emailService.sendPasswordResetEmail(member, code);
        return member.getId();
    }

    private Member validateResetPasswordRequest(MemberResetPasswordRequestDto requestDto) {
        Member member = retrieveMemberPort.findByIdOrThrow(requestDto.getId());
        if (!member.isSameName(requestDto.getName()) || !member.isSameEmail(requestDto.getEmail())) {
            throw new InvalidInformationException("올바르지 않은 정보입니다.");
        }
        return member;
    }
}
