package page.clab.api.domain.award.application.port.out;

import page.clab.api.domain.award.domain.Award;

public interface RemoveAwardPort {
    void delete(Award award);
}
