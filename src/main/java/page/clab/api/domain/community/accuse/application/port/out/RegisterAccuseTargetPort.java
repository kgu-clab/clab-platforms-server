package page.clab.api.domain.community.accuse.application.port.out;

import page.clab.api.domain.community.accuse.domain.AccuseTarget;

public interface RegisterAccuseTargetPort {

    AccuseTarget save(AccuseTarget accuseTarget);
}
