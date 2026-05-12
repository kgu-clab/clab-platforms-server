package page.clab.api.domain.library.bookLoanRecord.application.port.in;

public interface CancelBookLoanUseCase {

    Long cancelBookLoan(Long bookLoanRecordId);
}
