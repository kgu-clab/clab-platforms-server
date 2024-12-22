package page.clab.api.domain.community.hashtag.application.port.out;

import page.clab.api.domain.community.hashtag.domain.Hashtag;

public interface RegisterHashtagPort {

    Hashtag save(Hashtag hashtag);
}
