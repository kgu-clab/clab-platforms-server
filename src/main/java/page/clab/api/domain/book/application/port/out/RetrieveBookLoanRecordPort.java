package page.clab.api.domain.book.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.book.application.dto.response.BookLoanRecordOverdueResponseDto;
import page.clab.api.domain.book.application.dto.response.BookLoanRecordResponseDto;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.domain.book.domain.BookLoanStatus;

import java.util.Optional;

public interface RetrieveBookLoanRecordPort {
    Optional<BookLoanRecord> findById(Long bookLoanRecordId);

    BookLoanRecord findByIdOrThrow(Long bookLoanRecordId);

    Page<BookLoanRecordResponseDto> findByConditions(Long bookId, String borrowerId, BookLoanStatus status, Pageable pageable);

    Page<BookLoanRecordOverdueResponseDto> findOverdueBookLoanRecords(Pageable pageable);

    Optional<BookLoanRecord> findByBookIdAndReturnedAtIsNullAndStatus(Long bookId, BookLoanStatus bookLoanStatus);

    BookLoanRecord findByBookIdAndReturnedAtIsNullAndStatusOrThrow(Long bookId, BookLoanStatus bookLoanStatus);

    Optional<BookLoanRecord> findByBookIdAndBorrowerIdAndStatus(Long bookId, String borrowerId, BookLoanStatus bookLoanStatus);
}
