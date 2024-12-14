package page.clab.api.domain.community.hashtag.application.port.out;

import page.clab.api.domain.community.hashtag.domain.Hashtag;

public interface RetrieveHashtagPort {

    Boolean existsByName(String name);

    Hashtag findByName(String name);

}
