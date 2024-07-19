package page.clab.api.domain.hiring.application.application.port.out;

import page.clab.api.domain.hiring.application.domain.Application;

public interface RemoveApplicationPort {
    void delete(Application application);
}
