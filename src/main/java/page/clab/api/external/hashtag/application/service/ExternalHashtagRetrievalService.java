package page.clab.api.external.hashtag.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.community.hashtag.application.port.in.RetrieveHashtagUseCase;
import page.clab.api.domain.community.hashtag.application.port.out.RetrieveHashtagPort;
import page.clab.api.domain.community.hashtag.domain.Hashtag;
import page.clab.api.external.hashtag.application.port.ExternalRetrieveHashtagUseCase;

@Service
@RequiredArgsConstructor
public class ExternalHashtagRetrievalService implements ExternalRetrieveHashtagUseCase {

    private final RetrieveHashtagUseCase retrieveHashtagUseCase;
    private final RetrieveHashtagPort retrieveHashtagPort;

    @Override
    public Hashtag getById(Long id) {
        return retrieveHashtagPort.getById(id);
    }

    @Override
    public Hashtag getByName(String name) {
        return retrieveHashtagUseCase.getByName(name);
    }

    @Override
    public Long getIdByName(String name) {
        return getByName(name).getId();
    }

    @Override
    public String getNameById(Long id) {
        return getById(id).getName();
    }
}
