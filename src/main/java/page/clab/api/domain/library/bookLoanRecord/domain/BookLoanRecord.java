package page.clab.api.domain.library.bookLoanRecord.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBorrowerInfoDto;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

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
    private Boolean isDeleted;

    public static BookLoanRecord create(Long bookId, MemberBorrowerInfoDto borrowerInfo) {
        return BookLoanRecord.builder()
            .bookId(bookId)
            .borrowerId(borrowerInfo.getMemberId())
            .loanExtensionCount(0L)
            .status(BookLoanStatus.PENDING)
            .isDeleted(false)
            .build();
    }

    public void markAsReturned(MemberBorrowerInfoDto borrowerInfo) {
        if (this.returnedAt != null) {
            throw new BaseException(ErrorCode.BOOK_ALREADY_RETURNED);
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
            throw new BaseException(ErrorCode.OVERDUE_BOOK_EXTENSION_NOT_ALLOWED);
        }
        if (this.loanExtensionCount >= MAX_EXTENSIONS) {
            throw new BaseException(ErrorCode.BOOK_LOAN_EXTENSION_LIMIT_EXCEEDED);
        }

        this.dueDate = this.dueDate.plusWeeks(2);
        this.loanExtensionCount += 1;
    }

    public void approve() {
        if (this.status != BookLoanStatus.PENDING) {
            throw new BaseException(ErrorCode.BOOK_LOAN_STATUS_NOT_PENDING);
        }
        this.status = BookLoanStatus.APPROVED;
        this.borrowedAt = LocalDateTime.now();
        this.dueDate = LocalDateTime.now().plusWeeks(1);
    }

    public void reject() {
        if (this.status != BookLoanStatus.PENDING) {
            throw new BaseException(ErrorCode.BOOK_LOAN_STATUS_NOT_PENDING);
        }
        this.status = BookLoanStatus.REJECTED;
    }
}
