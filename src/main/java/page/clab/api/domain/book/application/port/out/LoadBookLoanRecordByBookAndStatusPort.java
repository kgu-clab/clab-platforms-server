package page.clab.api.domain.book.application.port.out;

import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.domain.book.domain.BookLoanStatus;

import java.util.Optional;

public interface LoadBookLoanRecordByBookAndStatusPort {
    Optional<BookLoanRecord> findByBookAndReturnedAtIsNullAndStatus(Book book, BookLoanStatus bookLoanStatus);
    BookLoanRecord findByBookAndReturnedAtIsNullAndStatusOrThrow(Book book, BookLoanStatus bookLoanStatus);
    Optional<BookLoanRecord> findByBookAndBorrowerIdAndStatus(Book book, String borrowerId, BookLoanStatus bookLoanStatus);
    BookLoanRecord findByBookAndBorrowerIdAndStatusOrThrow(Book book, String borrowerId, BookLoanStatus bookLoanStatus);
}
