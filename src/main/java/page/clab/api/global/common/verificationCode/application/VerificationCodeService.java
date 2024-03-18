package page.clab.api.global.common.verificationCode.application;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.verificationCode.dao.VerificationCodeRepository;
import page.clab.api.global.common.verificationCode.domain.VerificationCode;
import page.clab.api.global.common.verificationCode.dto.request.VerificationCodeRequestDto;
import page.clab.api.global.exception.NotFoundException;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;

    public VerificationCode getVerificationCode(String verificationCode) {
        return verificationCodeRepository.findByVerificationCode(verificationCode)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 인증코드입니다."));
    }
    
    public void saveVerificationCode(String memberId, String verificationCode) {
        VerificationCode code = VerificationCode.create(memberId, verificationCode);
        verificationCodeRepository.save(code);
    }

    public void deleteVerificationCode(String verificationCode) {
        verificationCodeRepository.findByVerificationCode(verificationCode)
                .ifPresent(verificationCodeRepository::delete);
    }

    public VerificationCode validateVerificationCode(VerificationCodeRequestDto verificationCodeRequestDto, Member member) {
        VerificationCode verificationCode = getVerificationCode(verificationCodeRequestDto.getVerificationCode());
        verificationCode.validateRequest(member.getId());
        return verificationCode;
    }

    public String generateVerificationCode() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] codeBytes = new byte[9];
        secureRandom.nextBytes(codeBytes);
        return Base64.encodeBase64URLSafeString(codeBytes);
    }

}