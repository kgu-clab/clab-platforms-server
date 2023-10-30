package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.type.entity.Book;
import page.clab.api.type.entity.BookLoanRecord;
import page.clab.api.type.entity.Member;

import java.util.List;
import java.util.Optional;

public interface BookLoanRecordRepository extends JpaRepository<BookLoanRecord, Long> {

    Optional<BookLoanRecord> findByBookAndBorrowerAndReturnedAtIsNull(Book book, Member borrower);

    List<BookLoanRecord> findByBook_Id(Long bookId);

    List<BookLoanRecord> findByBorrower_Id(String borrowerId);

    List<BookLoanRecord> findByBook_IdAndBorrower_Id(Long bookId, String borrowerId);

    List<BookLoanRecord> findByReturnedAtIsNull();

}
