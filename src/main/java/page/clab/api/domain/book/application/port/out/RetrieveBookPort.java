package page.clab.api.domain.book.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.book.domain.Book;

import java.util.Optional;

public interface RetrieveBookPort {
    Optional<Book> findById(Long bookId);

    Book findByIdOrThrow(Long bookId);

    Page<Book> findAllByIsDeletedTrue(Pageable pageable);

    Page<Book> findByConditions(String title, String category, String publisher, String borrowerId, String borrowerName, Pageable pageable);

    int countByBorrowerId(String borrowerId);
}
