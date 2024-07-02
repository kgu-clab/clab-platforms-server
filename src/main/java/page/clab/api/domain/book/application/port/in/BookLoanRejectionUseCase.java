package page.clab.api.domain.book.application.port.in;

public interface BookLoanRejectionUseCase {
    Long reject(Long bookLoanRecordId);
}