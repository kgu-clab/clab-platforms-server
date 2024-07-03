package page.clab.api.domain.member.application.port.in;

import java.time.LocalDateTime;

public interface MemberUpdateUseCase {
    void updateLoanSuspensionDate(String memberId, LocalDateTime loanSuspensionDate);
    void updateLastLoginTime(String id);
}
