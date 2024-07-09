package page.clab.api.domain.book.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.domain.book.domain.BookLoanStatus;

import java.util.Optional;

public interface BookLoanRecordRepository extends JpaRepository<BookLoanRecord, Long>, BookLoanRecordRepositoryCustom {
    Optional<BookLoanRecord> findByBookAndReturnedAtIsNullAndStatus(Book book, BookLoanStatus bookLoanStatus);

    Optional<BookLoanRecord> findByBookAndBorrowerIdAndStatus(Book book, String borrowerId, BookLoanStatus bookLoanStatus);
}
