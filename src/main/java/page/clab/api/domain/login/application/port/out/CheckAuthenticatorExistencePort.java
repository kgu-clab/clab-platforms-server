package page.clab.api.domain.login.application.port.out;

public interface CheckAuthenticatorExistencePort {
    boolean existsById(String memberId);
}
