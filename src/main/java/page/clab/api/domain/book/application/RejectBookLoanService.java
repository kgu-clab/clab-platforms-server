package page.clab.api.domain.book.application;

public interface RejectBookLoanService {
    Long execute(Long bookLoanRecordId);
}