package page.clab.api.domain.book.application;

public interface BookLoanRejectionService {
    Long reject(Long bookLoanRecordId);
}