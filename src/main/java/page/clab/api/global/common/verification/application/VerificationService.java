package page.clab.api.global.common.verification.application;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.verification.dao.VerificationRepository;
import page.clab.api.global.common.verification.domain.Verification;
import page.clab.api.global.common.verification.dto.request.VerificationRequestDto;
import page.clab.api.global.exception.NotFoundException;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationRepository verificationRepository;

    public Verification getVerificationCode(String verificationCode) {
        return verificationRepository.findByVerificationCode(verificationCode)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 인증코드입니다."));
    }
    
    public void saveVerificationCode(String memberId, String verificationCode) {
        Verification code = Verification.create(memberId, verificationCode);
        verificationRepository.save(code);
    }

    public void deleteVerificationCode(String verificationCode) {
        verificationRepository.findByVerificationCode(verificationCode)
                .ifPresent(verificationRepository::delete);
    }

    public Verification validateVerificationCode(VerificationRequestDto verificationRequestDto, Member member) {
        Verification verification = getVerificationCode(verificationRequestDto.getVerificationCode());
        verification.validateRequest(member.getId());
        return verification;
    }

    public String generateVerificationCode() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] codeBytes = new byte[9];
        secureRandom.nextBytes(codeBytes);
        return Base64.encodeBase64URLSafeString(codeBytes);
    }

}