package page.clab.api.domain.members.support.application.port.out;

import page.clab.api.domain.members.support.domain.Support;

public interface RegisterSupportPort {

    Support save(Support support);
}
