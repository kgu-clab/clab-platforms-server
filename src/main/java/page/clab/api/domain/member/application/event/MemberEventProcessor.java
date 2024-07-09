package page.clab.api.domain.member.application.event;

public interface MemberEventProcessor {

    void processMemberDeleted(String memberId);

    void processMemberUpdated(String memberId);

}
