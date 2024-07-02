package page.clab.api.domain.book.application.port.out;

import page.clab.api.domain.book.domain.BookLoanRecord;

import java.util.Optional;

public interface LoadBookLoanRecordPort {
    Optional<BookLoanRecord> findById(Long bookLoanRecordId);
    BookLoanRecord findByIdOrThrow(Long bookLoanRecordId);
}
