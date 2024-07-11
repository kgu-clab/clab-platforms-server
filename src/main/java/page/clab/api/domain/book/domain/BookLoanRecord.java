package page.clab.api.domain.book.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.book.application.exception.BookAlreadyReturnedException;
import page.clab.api.domain.book.application.exception.LoanNotPendingException;
import page.clab.api.domain.book.application.exception.LoanSuspensionException;
import page.clab.api.domain.book.application.exception.OverdueException;
import page.clab.api.domain.member.application.dto.shared.MemberBorrowerInfoDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BookLoanRecord {

    private Long id;
    private Long bookId;
    private String borrowerId;
    private LocalDateTime borrowedAt;
    private LocalDateTime returnedAt;
    private LocalDateTime dueDate;
    private Long loanExtensionCount;
    private BookLoanStatus status;

    public static BookLoanRecord create(Long bookId, MemberBorrowerInfoDto borrowerInfo) {
        return BookLoanRecord.builder()
                .bookId(bookId)
                .borrowerId(borrowerInfo.getMemberId())
                .loanExtensionCount(0L)
                .status(BookLoanStatus.PENDING)
                .build();
    }

    public void markAsReturned(MemberBorrowerInfoDto borrowerInfo) {
        if (this.returnedAt != null) {
            throw new BookAlreadyReturnedException("이미 반납된 도서입니다.");
        }
        this.returnedAt = LocalDateTime.now();
        if (isOverdue(returnedAt)) {
            long overdueDays = ChronoUnit.DAYS.between(this.dueDate, this.returnedAt);
            borrowerInfo.handleOverdueAndSuspension(overdueDays);
        }
        this.status = BookLoanStatus.RETURNED;
    }

    private boolean isOverdue(LocalDateTime returnedAt) {
        return returnedAt.isAfter(this.dueDate);
    }

    public void extendLoan(MemberBorrowerInfoDto borrowerInfo) {
        final long MAX_EXTENSIONS = 2;
        LocalDateTime now = LocalDateTime.now();

        borrowerInfo.checkLoanSuspension();

        if (now.isAfter(this.dueDate)) {
            throw new LoanSuspensionException("연체 중인 도서는 연장할 수 없습니다.");
        }
        if (this.loanExtensionCount >= MAX_EXTENSIONS) {
            throw new OverdueException("대출 연장 횟수를 초과했습니다.");
        }

        this.dueDate = this.dueDate.plusWeeks(2);
        this.loanExtensionCount += 1;
    }

    public void approve() {
        if (this.status != BookLoanStatus.PENDING) {
            throw new LoanNotPendingException("대출 신청 상태가 아닙니다.");
        }
        this.status = BookLoanStatus.APPROVED;
        this.borrowedAt = LocalDateTime.now();
        this.dueDate = LocalDateTime.now().plusWeeks(1);
    }

    public void reject() {
        if (this.status != BookLoanStatus.PENDING) {
            throw new LoanNotPendingException("대출 신청 상태가 아닙니다.");
        }
        this.status = BookLoanStatus.REJECTED;
    }
}
