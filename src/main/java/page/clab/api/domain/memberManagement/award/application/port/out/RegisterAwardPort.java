package page.clab.api.domain.memberManagement.award.application.port.out;

import page.clab.api.domain.memberManagement.award.domain.Award;

import java.util.List;

public interface RegisterAwardPort {

    Award save(Award award);

    void saveAll(List<Award> awards);
}
