package page.clab.api.external.memberManagement.member.application.port;

import java.time.LocalDateTime;

public interface ExternalUpdateMemberUseCase {

    void updateLastLoginTime(String id);

    void updateLoanSuspensionDate(String memberId, LocalDateTime loanSuspensionDate);
}
