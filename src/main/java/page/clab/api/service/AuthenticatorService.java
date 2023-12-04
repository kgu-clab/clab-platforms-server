package page.clab.api.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.repository.AuthenticatorRepository;
import page.clab.api.type.entity.Authenticator;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticatorService {

    private final AuthenticatorRepository authenticatorRepository;

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

    public boolean isAuthenticatorValid(String memberId, String totp) {
        return isAuthenticatorExist(memberId) && validateTotp(memberId, totp);
    }

    public boolean validateTotp(String memberId, String totp) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        Authenticator authenticator = authenticatorRepository.getById(memberId);
        String secretKey = authenticator.getSecretKey();
        return gAuth.authorize(secretKey, Integer.parseInt(totp));
    }

    public Authenticator getAuthenticatorById(String memberId) {
        return authenticatorRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("해당 아이디에 대한 정보가 존재하지 않습니다."));
    }

    public boolean isAuthenticatorExist(String memberId) {
        return authenticatorRepository.existsById(memberId);
    }

}
