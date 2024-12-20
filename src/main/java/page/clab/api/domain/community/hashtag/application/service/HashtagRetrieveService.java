package page.clab.api.domain.community.hashtag.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.community.hashtag.application.port.in.RetrieveHashtagUseCase;
import page.clab.api.domain.community.hashtag.application.port.out.RetrieveHashtagPort;
import page.clab.api.domain.community.hashtag.domain.Hashtag;

@Service
@RequiredArgsConstructor
public class HashtagRetrieveService implements RetrieveHashtagUseCase {

    private final RetrieveHashtagPort retrieveHashtagPort;

    @Override
    public Boolean existsByName(String name) {
        return retrieveHashtagPort.existsByName(name);
    }

    @Override
    public Hashtag getByName(String name) {
        return retrieveHashtagPort.findByName(name);
    }
}
