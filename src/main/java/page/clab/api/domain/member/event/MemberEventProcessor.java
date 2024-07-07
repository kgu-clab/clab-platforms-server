package page.clab.api.domain.member.event;

public interface MemberEventProcessor {

    void processMemberDeleted(String memberId);

    void processMemberUpdated(String memberId);

}
