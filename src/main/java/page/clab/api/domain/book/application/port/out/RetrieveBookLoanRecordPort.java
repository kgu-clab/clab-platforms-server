package page.clab.api.domain.book.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.domain.book.domain.BookLoanStatus;
import page.clab.api.domain.book.dto.response.BookLoanRecordOverdueResponseDto;
import page.clab.api.domain.book.dto.response.BookLoanRecordResponseDto;

import java.util.Optional;

public interface RetrieveBookLoanRecordPort {
    Optional<BookLoanRecord> findById(Long bookLoanRecordId);
    BookLoanRecord findByIdOrThrow(Long bookLoanRecordId);
    Page<BookLoanRecordResponseDto> findByConditions(Long bookId, String borrowerId, BookLoanStatus status, Pageable pageable);
    Page<BookLoanRecordOverdueResponseDto> findOverdueBookLoanRecords(Pageable pageable);
    Optional<BookLoanRecord> findByBookAndReturnedAtIsNullAndStatus(Book book, BookLoanStatus bookLoanStatus);
    BookLoanRecord findByBookAndReturnedAtIsNullAndStatusOrThrow(Book book, BookLoanStatus bookLoanStatus);
    Optional<BookLoanRecord> findByBookAndBorrowerIdAndStatus(Book book, String borrowerId, BookLoanStatus bookLoanStatus);
}
