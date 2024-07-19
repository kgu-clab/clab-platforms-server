package page.clab.api.domain.community.accuse.application.port.out;

import page.clab.api.domain.community.accuse.domain.Accuse;

import java.util.List;

public interface RegisterAccusePort {

    Accuse save(Accuse accuse);

    void saveAll(List<Accuse> accuses);
}
