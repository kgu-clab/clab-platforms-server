package page.clab.api.domain.memberManagement.member.application.event;

import page.clab.api.domain.hiring.application.domain.Application;

public interface MemberEventProcessor {

    void processMemberDeleted(String memberId);

    void processMemberUpdated(String memberId);

    void processMemberCreated(Application application);
}
