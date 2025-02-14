package page.clab.api.global.common.verification.application;

import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.global.common.verification.dao.VerificationRepository;
import page.clab.api.global.common.verification.domain.Verification;
import page.clab.api.global.common.verification.dto.request.VerificationRequestDto;
import page.clab.api.global.exception.NotFoundException;

/**
 * {@code VerificationService}는 회원 인증 코드 생성, 저장, 검증 및 삭제 기능을 제공하는 서비스입니다.
 *
 * <p>인증 코드는 사용자의 이메일 또는 전화번호 인증을 위한 임시 코드로,
 * 생성된 코드는 데이터베이스에 저장되고, 유효성 검사를 통해 해당 사용자의 인증을 검증합니다.</p>
 * <p>
 * 주요 기능:
 * <ul>
 *     <li>{@link #getVerificationCode(String)} - 주어진 코드로 데이터베이스에서 인증 코드를 조회합니다.</li>
 *     <li>{@link #saveVerificationCode(String, String)} - 주어진 회원 ID와 인증 코드를 데이터베이스에 저장합니다.</li>
 *     <li>{@link #deleteVerificationCode(String)} - 주어진 인증 코드를 데이터베이스에서 삭제합니다.</li>
 *     <li>{@link #validateVerificationCode(VerificationRequestDto, Member)} - 요청된 코드와 사용자를 검증합니다.</li>
 *     <li>{@link #generateVerificationCode()} - 보안 난수를 기반으로 새로운 인증 코드를 생성합니다.</li>
 * </ul>
 */
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
