package page.clab.api.domain.memberManagement.award.application.port.out;

import java.util.List;
import page.clab.api.domain.memberManagement.award.domain.Award;

public interface RegisterAwardPort {

    Award save(Award award);

    void saveAll(List<Award> awards);
}
