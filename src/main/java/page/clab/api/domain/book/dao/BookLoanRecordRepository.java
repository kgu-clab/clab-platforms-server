package page.clab.api.domain.book.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.BookLoanRecord;

import java.util.Optional;

public interface BookLoanRecordRepository extends JpaRepository<BookLoanRecord, Long> {

    Page<BookLoanRecord> findAllByOrderByBorrowedAtDesc(Pageable pageable);

    Optional<BookLoanRecord> findByBookAndReturnedAtIsNull(Book book);

    Page<BookLoanRecord> findByBook_IdOrderByBorrowedAtDesc(Long bookId, Pageable pageable);

    Page<BookLoanRecord> findByBorrower_IdOrderByBorrowedAtDesc(String borrowerId, Pageable pageable);

    Page<BookLoanRecord> findByBook_IdAndBorrower_IdOrderByBorrowedAtDesc(Long bookId, String borrowerId, Pageable pageable);

    Page<BookLoanRecord> findByReturnedAtIsNullOrderByBorrowedAtDesc(Pageable pageable);

}
