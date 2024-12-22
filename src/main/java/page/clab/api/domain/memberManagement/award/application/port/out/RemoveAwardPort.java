package page.clab.api.domain.memberManagement.award.application.port.out;

import page.clab.api.domain.memberManagement.award.domain.Award;

public interface RemoveAwardPort {

    void delete(Award award);
}
