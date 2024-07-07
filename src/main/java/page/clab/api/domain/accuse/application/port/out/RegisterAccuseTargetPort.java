package page.clab.api.domain.accuse.application.port.out;

import page.clab.api.domain.accuse.domain.AccuseTarget;

public interface RegisterAccuseTargetPort {
    AccuseTarget save(AccuseTarget accuseTarget);
}
