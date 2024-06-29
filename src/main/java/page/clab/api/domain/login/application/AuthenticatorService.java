package page.clab.api.domain.login.application;

public interface AuthenticatorService {
    boolean isAuthenticatorValid(String memberId, String totp);
    boolean isAuthenticatorExist(String memberId);
    String generateSecretKey(String memberId);
    String resetAuthenticator(String memberId);
}
