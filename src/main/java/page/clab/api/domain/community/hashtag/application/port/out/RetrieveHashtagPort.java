package page.clab.api.domain.community.hashtag.application.port.out;

import java.util.List;
import page.clab.api.domain.community.hashtag.domain.Hashtag;

public interface RetrieveHashtagPort {

    Boolean existsByName(String name);

    Hashtag findByName(String name);

    Hashtag getById(Long id);

    Boolean existsById(Long id);

    List<Hashtag> findAll();
}
