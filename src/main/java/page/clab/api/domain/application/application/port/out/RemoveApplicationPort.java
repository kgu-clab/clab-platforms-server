package page.clab.api.domain.application.application.port.out;

import page.clab.api.domain.application.domain.Application;

public interface RemoveApplicationPort {
    void delete(Application application);
}
