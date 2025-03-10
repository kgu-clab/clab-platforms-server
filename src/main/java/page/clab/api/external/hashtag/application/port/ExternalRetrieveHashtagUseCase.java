package page.clab.api.external.hashtag.application.port;

import page.clab.api.domain.community.hashtag.domain.Hashtag;

public interface ExternalRetrieveHashtagUseCase {

    Hashtag getById(Long id);

    Hashtag getByName(String name);

    Long getIdByName(String name);

    String getNameById(Long id);
}
