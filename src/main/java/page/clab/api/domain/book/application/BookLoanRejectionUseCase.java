package page.clab.api.domain.book.application;

public interface BookLoanRejectionUseCase {
    Long reject(Long bookLoanRecordId);
}