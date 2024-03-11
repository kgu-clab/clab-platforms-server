package page.clab.api.domain.book.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.BookLoanRecord;

import java.util.Optional;

public interface BookLoanRecordRepository extends JpaRepository<BookLoanRecord, Long>, QBookLoanRecordRepository {

    Optional<BookLoanRecord> findByBookAndReturnedAtIsNull(Book book);

}
