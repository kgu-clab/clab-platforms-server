package page.clab.api.domain.community.hashtag.application.port.in;

import page.clab.api.domain.community.hashtag.domain.Hashtag;

public interface RetrieveHashtagUseCase {

    Boolean existsByName(String name);

    Hashtag getByName(String name);
}
