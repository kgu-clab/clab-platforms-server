package page.clab.api.domain.login.application;

public interface AuthenticatorService {
    String generateSecretKey(String memberId);
    boolean isAuthenticatorValid(String memberId, String totp);
    String resetAuthenticator(String memberId);
    boolean isAuthenticatorExist(String memberId);
}