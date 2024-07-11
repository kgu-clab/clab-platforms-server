package page.clab.api.domain.accuse.application.port.out;

import page.clab.api.domain.accuse.domain.Accuse;

import java.util.List;

public interface RegisterAccusePort {
    Accuse save(Accuse accuse);

    void saveAll(List<Accuse> accuses);
}
