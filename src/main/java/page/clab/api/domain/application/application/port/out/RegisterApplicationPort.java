package page.clab.api.domain.application.application.port.out;

import page.clab.api.domain.application.domain.Application;

public interface RegisterApplicationPort {
    Application save(Application application);
}
