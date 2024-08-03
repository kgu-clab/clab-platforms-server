package page.clab.api.domain.auth.login.application.port.in;

public interface ManageAuthenticatorUseCase {

    boolean isAuthenticatorValid(String memberId, String totp);

    boolean isAuthenticatorExist(String memberId);

    String generateSecretKey(String memberId);

    String resetAuthenticator(String memberId);
}
