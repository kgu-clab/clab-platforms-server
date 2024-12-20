package page.clab.api.external.hashtag.application.port;

import page.clab.api.domain.community.hashtag.domain.Hashtag;

public interface ExternalRetrieveHashtagUseCase {
    Hashtag getById(Long id);

    Boolean existById(Long id);

    Hashtag getByName(String name);
}
