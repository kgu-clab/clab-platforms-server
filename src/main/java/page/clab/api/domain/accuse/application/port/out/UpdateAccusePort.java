package page.clab.api.domain.accuse.application.port.out;

import page.clab.api.domain.accuse.domain.Accuse;

public interface UpdateAccusePort {
    Accuse update(Accuse accuse);
}
