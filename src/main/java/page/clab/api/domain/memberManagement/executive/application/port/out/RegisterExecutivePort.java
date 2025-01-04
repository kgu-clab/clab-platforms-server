package page.clab.api.domain.memberManagement.executive.application.port.out;

import page.clab.api.domain.memberManagement.executive.domain.Executive;

public interface RegisterExecutivePort {
    Executive save(Executive executive);
}
