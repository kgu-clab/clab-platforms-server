package page.clab.api.domain.library.bookLoanRecord.adapter.out.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanStatus;

@Repository
public interface BookLoanRecordRepository extends JpaRepository<BookLoanRecordJpaEntity, Long>,
    BookLoanRecordRepositoryCustom {

    Optional<BookLoanRecordJpaEntity> findByBookIdAndReturnedAtIsNullAndStatus(Long bookId,
        BookLoanStatus bookLoanStatus);

    Optional<BookLoanRecordJpaEntity> findByBookIdAndBorrowerIdAndStatus(Long bookId, String borrowerId,
        BookLoanStatus bookLoanStatus);
}
