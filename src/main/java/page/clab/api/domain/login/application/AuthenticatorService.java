package page.clab.api.domain.login.application;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.login.application.port.in.ManageAuthenticatorUseCase;
import page.clab.api.domain.login.application.port.out.CheckAuthenticatorExistencePort;
import page.clab.api.domain.login.application.port.out.LoadAuthenticatorPort;
import page.clab.api.domain.login.application.port.out.RegisterAuthenticatorPort;
import page.clab.api.domain.login.application.port.out.RemoveAuthenticatorPort;
import page.clab.api.domain.login.domain.Authenticator;
import page.clab.api.global.util.EncryptionUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticatorService implements ManageAuthenticatorUseCase {

    private final RegisterAuthenticatorPort registerAuthenticatorPort;
    private final LoadAuthenticatorPort loadAuthenticatorPort;
    private final RemoveAuthenticatorPort removeAuthenticatorPort;
    private final CheckAuthenticatorExistencePort checkAuthenticatorExistencePort;
    private final GoogleAuthenticator googleAuthenticator;
    private final EncryptionUtil encryptionUtil;

    @Transactional
    @Override
    public String generateSecretKey(String memberId) {
        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        String secretKey = key.getKey();
        saveAuthenticator(memberId, secretKey);
        return secretKey;
    }

    @Override
    public boolean isAuthenticatorValid(String memberId, String totp) {
        Authenticator authenticator = loadAuthenticatorPort.findById(memberId).orElse(null);
        return authenticator != null && validateTotp(authenticator, totp);
    }

    private boolean validateTotp(Authenticator authenticator, String totp) {
        String secretKey = encryptionUtil.decrypt(authenticator.getSecretKey());
        return googleAuthenticator.authorize(secretKey, Integer.parseInt(totp));
    }

    @Override
    public String resetAuthenticator(String memberId) {
        Authenticator authenticator = loadAuthenticatorPort.findByIdOrThrow(memberId);
        removeAuthenticatorPort.delete(authenticator);
        return memberId;
    }

    private void saveAuthenticator(String memberId, String secretKey) {
        Authenticator authenticator = Authenticator.create(memberId, encryptionUtil.encrypt(secretKey));
        registerAuthenticatorPort.save(authenticator);
    }

    @Override
    public boolean isAuthenticatorExist(String memberId) {
        return checkAuthenticatorExistencePort.existsById(memberId);
    }
}
