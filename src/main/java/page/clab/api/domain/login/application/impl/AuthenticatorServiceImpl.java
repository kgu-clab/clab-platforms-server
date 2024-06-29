package page.clab.api.domain.login.application.impl;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.login.application.AuthenticatorService;
import page.clab.api.domain.login.dao.AuthenticatorRepository;
import page.clab.api.domain.login.domain.Authenticator;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.util.EncryptionUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticatorServiceImpl implements AuthenticatorService {

    private final AuthenticatorRepository authenticatorRepository;

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
        Authenticator authenticator = getAuthenticatorById(memberId);
        return authenticator != null && validateTotp(authenticator, totp);
    }

    private boolean validateTotp(Authenticator authenticator, String totp) {
        String secretKey = encryptionUtil.decrypt(authenticator.getSecretKey());
        return googleAuthenticator.authorize(secretKey, Integer.parseInt(totp));
    }

    @Override
    public String resetAuthenticator(String memberId) {
        Authenticator authenticator = getAuthenticatorByIdOrThrow(memberId);
        authenticatorRepository.delete(authenticator);
        return memberId;
    }

    private Authenticator getAuthenticatorById(String memberId) {
        return authenticatorRepository.findById(memberId).orElse(null);
    }

    public Authenticator getAuthenticatorByIdOrThrow(String memberId) {
        return authenticatorRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("해당 멤버의 Authenticator가 존재하지 않습니다."));
    }

    private void saveAuthenticator(String memberId, String secretKey) {
        Authenticator authenticator = Authenticator.create(memberId, encryptionUtil.encrypt(secretKey));
        authenticatorRepository.save(authenticator);
    }

    @Override
    public boolean isAuthenticatorExist(String memberId) {
        return authenticatorRepository.existsById(memberId);
    }
}
