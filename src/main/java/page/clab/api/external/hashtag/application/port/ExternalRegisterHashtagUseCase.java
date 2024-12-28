package page.clab.api.external.hashtag.application.port;

import page.clab.api.domain.community.hashtag.domain.Hashtag;

public interface ExternalRegisterHashtagUseCase {

    Hashtag save(Hashtag hashtag);
}
