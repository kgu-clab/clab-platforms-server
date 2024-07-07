package page.clab.api.domain.book.application.port.in;

public interface RejectBookLoanUseCase {
    Long rejectBookLoan(Long bookLoanRecordId);
}
