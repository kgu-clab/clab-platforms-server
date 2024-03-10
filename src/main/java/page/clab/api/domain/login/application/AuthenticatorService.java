package page.clab.api.domain.login.application;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import page.clab.api.domain.login.dao.AuthenticatorRepository;
import page.clab.api.domain.login.domain.Authenticator;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.util.EncryptionUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticatorService {

    private final AuthenticatorRepository authenticatorRepository;

    private final EncryptionUtil encryptionUtil;

    public String generateSecretKey(String memberId) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        String secretKey = key.getKey();
        Authenticator authenticator = Authenticator.builder()
                .memberId(memberId)
                .secretKey(encryptionUtil.encrypt(secretKey))
                .build();
        authenticatorRepository.save(authenticator);
        return secretKey;
    }

    public boolean isAuthenticatorValid(String memberId, String totp) {
        return isAuthenticatorExist(memberId) && validateTotp(memberId, totp);
    }

    public boolean validateTotp(String memberId, String totp) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        Authenticator authenticator = getAuthenticatorByIdOrThrow(memberId);
        String secretKey = encryptionUtil.decrypt(authenticator.getSecretKey());
        return gAuth.authorize(secretKey, Integer.parseInt(totp));
    }

    public String resetAuthenticator(String memberId) {
        Authenticator authenticator = getAuthenticatorByIdOrThrow(memberId);
        authenticatorRepository.deleteById(memberId);
        return authenticator.getMemberId();
    }

    public Authenticator getAuthenticatorByIdOrThrow(String memberId) {
        return authenticatorRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("해당 멤버의 Authenticator가 존재하지 않습니다."));
    }

    public boolean isAuthenticatorExist(String memberId) {
        return authenticatorRepository.existsById(memberId);
    }

}
