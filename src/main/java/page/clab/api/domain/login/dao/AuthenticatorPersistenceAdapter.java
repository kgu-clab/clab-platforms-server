package page.clab.api.domain.login.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.login.application.port.out.RegisterAuthenticatorPort;
import page.clab.api.domain.login.application.port.out.RemoveAuthenticatorPort;
import page.clab.api.domain.login.application.port.out.RetrieveAuthenticatorPort;
import page.clab.api.domain.login.domain.Authenticator;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthenticatorPersistenceAdapter implements
        RegisterAuthenticatorPort,
        RetrieveAuthenticatorPort,
        RemoveAuthenticatorPort {

    private final AuthenticatorRepository authenticatorRepository;

    @Override
    public Authenticator save(Authenticator authenticator) {
        return authenticatorRepository.save(authenticator);
    }

    @Override
    public Optional<Authenticator> findById(String memberId) {
        return authenticatorRepository.findById(memberId);
    }

    @Override
    public Authenticator findByIdOrThrow(String memberId) {
        return authenticatorRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("[Authenticator] memberId: " + memberId + "에 해당하는 Authenticator가 존재하지 않습니다."));
    }

    @Override
    public void delete(Authenticator authenticator) {
        authenticatorRepository.delete(authenticator);
    }

    @Override
    public boolean existsById(String memberId) {
        return authenticatorRepository.existsById(memberId);
    }
}
