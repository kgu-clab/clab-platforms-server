package page.clab.api.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import page.clab.api.repository.AuthenticatorRepository;
import page.clab.api.type.entity.Authenticator;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticatorService {

    private final AuthenticatorRepository authenticatorRepository;

    private final String issuer = "C-Lab";

    public String generateSecretKey(String memberId) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        String secretKey = key.getKey();
        Authenticator authenticator = Authenticator.builder()
                .memberId(memberId)
                .secretKey(secretKey)
                .build();
        authenticatorRepository.save(authenticator);
        return secretKey;
    }

    public String generateSecretKeyQRCodeUrl(String accountName) {
        Authenticator authenticator = authenticatorRepository.getById(accountName);
        String secretKey = authenticator.getSecretKey();
        GoogleAuthenticatorKey googleAuthenticatorKey = new GoogleAuthenticatorKey.Builder(secretKey).build();
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL(issuer, accountName, googleAuthenticatorKey);
    }

    public boolean isAuthenticatorValid(String memberId, String totp) {
        return isAuthenticatorExist(memberId) && validateTotp(memberId, totp);
    }

    public boolean validateTotp(String memberId, String totp) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        Authenticator authenticator = authenticatorRepository.getById(memberId);
        String secretKey = authenticator.getSecretKey();
        return gAuth.authorize(secretKey, Integer.parseInt(totp));
    }

    public boolean isAuthenticatorExist(String memberId) {
        return authenticatorRepository.existsById(memberId);
    }

    public String resetAuthenticator(String memberId) {
        Authenticator authenticator = authenticatorRepository.getById(memberId);
        authenticatorRepository.deleteById(memberId);
        return authenticator.getMemberId();
    }

}
