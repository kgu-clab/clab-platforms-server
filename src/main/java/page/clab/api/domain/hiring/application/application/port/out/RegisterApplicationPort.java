package page.clab.api.domain.hiring.application.application.port.out;

import page.clab.api.domain.hiring.application.domain.Application;

public interface RegisterApplicationPort {

    Application save(Application application);
}
