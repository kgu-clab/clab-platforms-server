package page.clab.api.domain.book.dao;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.domain.member.domain.Member;

public interface BookLoanRecordRepository extends JpaRepository<BookLoanRecord, Long> {

    Page<BookLoanRecord> findAllByOrderByBorrowedAtDesc(Pageable pageable);

    Optional<BookLoanRecord> findByBookAndBorrowerAndReturnedAtIsNull(Book book, Member borrower);

    Page<BookLoanRecord> findByBook_IdOrderByBorrowedAtDesc(Long bookId, Pageable pageable);

    Page<BookLoanRecord> findByBorrower_IdOrderByBorrowedAtDesc(String borrowerId, Pageable pageable);

    Page<BookLoanRecord> findByBook_IdAndBorrower_IdOrderByBorrowedAtDesc(Long bookId, String borrowerId, Pageable pageable);

    Page<BookLoanRecord> findByReturnedAtIsNullOrderByBorrowedAtDesc(Pageable pageable);

}
