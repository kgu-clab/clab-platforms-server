package page.clab.api.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.type.entity.Book;
import page.clab.api.type.entity.BookLoanRecord;
import page.clab.api.type.entity.Member;

public interface BookLoanRecordRepository extends JpaRepository<BookLoanRecord, Long> {

    Optional<BookLoanRecord> findByBookAndBorrowerAndReturnedAtIsNull(Book book, Member borrower);

    Page<BookLoanRecord> findByBook_Id(Long bookId, Pageable pageable);

    Page<BookLoanRecord> findByBorrower_Id(String borrowerId, Pageable pageable);

    Page<BookLoanRecord> findByBook_IdAndBorrower_Id(Long bookId, String borrowerId, Pageable pageable);

    Page<BookLoanRecord> findByReturnedAtIsNull(Pageable pageable);

}
