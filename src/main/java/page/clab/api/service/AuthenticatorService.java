package page.clab.api.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import page.clab.api.repository.AuthenticatorRepository;
import page.clab.api.type.entity.Authenticator;

// ... (다른 import문들은 그대로 유지)

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticatorService {

    private final AuthenticatorRepository authenticatorRepository;

    private final String issuer = "CLAB";

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

    public String generateTotp(String memberId) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        Authenticator authenticator = authenticatorRepository.getById(memberId);
        String secretKey = authenticator.getSecretKey();
        int totp = gAuth.getTotpPassword(secretKey);
        return String.format("%06d", totp);
    }

    public String generateSecretKeyQRCodeUrl(String accountName) {
        Authenticator authenticator = authenticatorRepository.getById(accountName);
        String secretKey = authenticator.getSecretKey();
        GoogleAuthenticatorKey googleAuthenticatorKey = new GoogleAuthenticatorKey.Builder(secretKey).build();
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL(issuer, accountName, googleAuthenticatorKey);
    }

}
