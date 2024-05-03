package page.clab.api.domain.book.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.book.domain.Book;
import page.clab.api.domain.book.domain.BookLoanRecord;
import page.clab.api.domain.book.domain.BookLoanStatus;
import page.clab.api.domain.member.domain.Member;

import java.util.Optional;

public interface BookLoanRecordRepository extends JpaRepository<BookLoanRecord, Long>, BookLoanRecordRepositoryCustom {

    Optional<BookLoanRecord> findByBookAndReturnedAtIsNullAndStatus(Book book, BookLoanStatus bookLoanStatus);

    Optional<Object> findByBookAndBorrowerAndStatus(Book book, Member borrower, BookLoanStatus bookLoanStatus);

}
