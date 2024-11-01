package page.clab.api.domain.memberManagement.member.application.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.request.MemberResetPasswordRequestDto;
import page.clab.api.domain.memberManagement.member.application.port.in.ManageMemberPasswordUseCase;
import page.clab.api.domain.memberManagement.member.application.port.out.RegisterMemberPort;
import page.clab.api.domain.memberManagement.member.application.port.out.RetrieveMemberPort;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.global.common.email.application.EmailService;
import page.clab.api.global.common.verification.application.VerificationService;
import page.clab.api.global.common.verification.domain.Verification;
import page.clab.api.global.common.verification.dto.request.VerificationRequestDto;
import page.clab.api.global.exception.InvalidInformationException;

@Service
@RequiredArgsConstructor
public class MemberPasswordManagementService implements ManageMemberPasswordUseCase {

    private final RegisterMemberPort registerMemberPort;
    private final RetrieveMemberPort retrieveMemberPort;
    private final VerificationService verificationService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    // 멤버의 비밀번호를 재전송합니다.
    // 새롭게 생성된 비밀번호는 이메일을 통해 전송됩니다.
    @Transactional
    @Override
    public String resendMemberPassword(String memberId) {
        Member member = retrieveMemberPort.getById(memberId);

        String newPassword = verificationService.generateVerificationCode();
        member.updatePassword(newPassword, passwordEncoder);
        registerMemberPort.save(member);

        emailService.sendNewPasswordEmail(member, newPassword);
        return member.getId();
    }

    // 비밀번호 재설정을 요청합니다.
    // 비밀번호 재설정을 위한 인증 코드 생성 후, 이메일로 인증 코드를 전송합니다.
    @Transactional
    @Override
    public String requestMemberPasswordReset(MemberResetPasswordRequestDto requestDto) {
        Member member = validateResetPasswordRequest(requestDto);
        String code = verificationService.generateVerificationCode();
        verificationService.saveVerificationCode(member.getId(), code);
        emailService.sendPasswordResetCodeEmail(member, code);
        return member.getId();
    }

    // 비밀번호 재설정 인증을 처리합니다.
    // 사용자가 입력한 인증 코드를 검증한 후, 해당 코드를 새로운 비밀번호로 설정합니다.
    @Transactional
    @Override
    public String verifyMemberPasswordReset(VerificationRequestDto requestDto) {
        Member member = retrieveMemberPort.getById(requestDto.getMemberId());
        Verification verification = verificationService.validateVerificationCode(requestDto, member);
        updateMemberPasswordWithVerificationCode(verification.getVerificationCode(), member);
        return registerMemberPort.save(member).getId();
    }

    @Override
    public String generateOrRetrievePassword(String password) {
        return StringUtils.isEmpty(password)
                ? verificationService.generateVerificationCode()
                : password;
    }

    private Member validateResetPasswordRequest(MemberResetPasswordRequestDto requestDto) {
        Member member = retrieveMemberPort.getById(requestDto.getId());
        if (!member.isSameName(requestDto.getName()) || !member.isSameEmail(requestDto.getEmail())) {
            throw new InvalidInformationException("올바르지 않은 정보입니다.");
        }
        return member;
    }

    private void updateMemberPasswordWithVerificationCode(String verificationCode, Member member) {
        member.updatePassword(verificationCode, passwordEncoder);
        verificationService.deleteVerificationCode(verificationCode);
    }
}
