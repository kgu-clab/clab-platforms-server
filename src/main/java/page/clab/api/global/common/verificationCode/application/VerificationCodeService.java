package page.clab.api.global.common.verificationCode.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.global.common.verificationCode.dao.VerificationCodeRepository;
import page.clab.api.global.common.verificationCode.domain.VerificationCode;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;

    public VerificationCode getVerificationCode(String verificationCode) {
        return verificationCodeRepository.findByVerificationCode(verificationCode)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 인증코드입니다."));
    }
    
    public void saveVerificationCode(String memberId, String verificationCode) {
        VerificationCode code = VerificationCode.builder()
                .id(memberId)
                .verificationCode(verificationCode)
                .build();
        verificationCodeRepository.save(code);
    }

    public void deleteVerificationCode(String verificationCode) {
        verificationCodeRepository.findByVerificationCode(verificationCode)
                .ifPresent(verificationCodeRepository::delete);
    }

}