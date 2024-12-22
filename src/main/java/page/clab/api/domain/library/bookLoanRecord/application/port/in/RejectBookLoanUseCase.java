package page.clab.api.domain.library.bookLoanRecord.application.port.in;

public interface RejectBookLoanUseCase {

    Long rejectBookLoan(Long bookLoanRecordId);
}
