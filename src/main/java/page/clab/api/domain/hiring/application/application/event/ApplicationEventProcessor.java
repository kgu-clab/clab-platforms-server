package page.clab.api.domain.hiring.application.application.event;

import page.clab.api.domain.hiring.application.domain.Application;

public interface ApplicationEventProcessor {

    void processApplicationMemberCreated(Application application);

    void processPositionCreated(String memberId);
}
