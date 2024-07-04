package page.clab.api.domain.member.application.port.in;

import java.time.LocalDateTime;

public interface UpdateMemberUseCase {
    void updateLoanSuspensionDate(String memberId, LocalDateTime loanSuspensionDate);

    void updateLastLoginTime(String id);
}
