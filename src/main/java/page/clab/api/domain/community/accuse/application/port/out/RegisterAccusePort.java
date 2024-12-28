package page.clab.api.domain.community.accuse.application.port.out;

import java.util.List;
import page.clab.api.domain.community.accuse.domain.Accuse;

public interface RegisterAccusePort {

    Accuse save(Accuse accuse);

    void saveAll(List<Accuse> accuses);
}
