package page.clab.api.external.hashtag.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.community.hashtag.application.port.out.RegisterHashtagPort;
import page.clab.api.domain.community.hashtag.domain.Hashtag;
import page.clab.api.external.hashtag.application.port.ExternalRegisterHashtagUseCase;

@Service
@RequiredArgsConstructor
public class ExternalHashtagRegisterService implements ExternalRegisterHashtagUseCase {

    private final RegisterHashtagPort registerHashtagPort;

    @Override
    public Hashtag save(Hashtag hashtag) {
        return registerHashtagPort.save(hashtag);
    }
}
