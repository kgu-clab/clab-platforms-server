package page.clab.api.domain.memberManagement.member.application.dto.shared;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

@Getter
@Builder
public class MemberBorrowerInfoDto {

    private String memberId;
    private String memberName;
    private LocalDateTime loanSuspensionDate;

    public void handleOverdueAndSuspension(long overdueDays) {
        if (overdueDays > 0) {
            LocalDateTime currentDate = LocalDateTime.now();
            if (loanSuspensionDate == null || loanSuspensionDate.isBefore(currentDate)) {
                loanSuspensionDate = LocalDateTime.now().plusDays(overdueDays * 7);
            } else {
                loanSuspensionDate = loanSuspensionDate.plusDays(overdueDays * 7);
            }
        }
    }

    public void checkLoanSuspension() {
        if (loanSuspensionDate != null && LocalDateTime.now().isBefore(loanSuspensionDate)) {
            throw new BaseException(ErrorCode.BOOK_BORROWING_NOT_ALLOWED_DURING_SUSPENSION);
        }
    }
}
