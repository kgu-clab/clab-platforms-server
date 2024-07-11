package page.clab.api.domain.community.accuse.application.port.out;


import page.clab.api.domain.community.accuse.domain.Accuse;

public interface UpdateAccusePort {
    Accuse update(Accuse accuse);
}
