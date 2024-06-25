package page.clab.api.domain.member.dto.shared;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.book.exception.LoanSuspensionException;
import page.clab.api.domain.member.domain.Member;

import java.time.LocalDateTime;

@Getter
@Builder
public class BookBorrowerInfoDto {

    private String memberId;

    private String memberName;

    private LocalDateTime loanSuspensionDate;

    public static BookBorrowerInfoDto create(Member member) {
        return BookBorrowerInfoDto.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .loanSuspensionDate(member.getLoanSuspensionDate())
                .build();
    }

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
            throw new LoanSuspensionException("대출 정지 중입니다. 대출 정지일까지는 책을 대출할 수 없습니다.");
        }
    }

}
