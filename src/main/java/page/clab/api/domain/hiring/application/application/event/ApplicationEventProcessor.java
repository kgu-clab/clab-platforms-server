package page.clab.api.domain.hiring.application.application.event;

import page.clab.api.domain.hiring.application.application.dto.request.ApplicationMemberCreationDto;

public interface ApplicationEventProcessor {

    void processApplicationMemberCreated(ApplicationMemberCreationDto dto);

    void processPositionCreated(String memberId);
}
