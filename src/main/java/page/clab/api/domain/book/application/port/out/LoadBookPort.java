package page.clab.api.domain.book.application.port.out;

import page.clab.api.domain.book.domain.Book;

import java.util.Optional;

public interface LoadBookPort {
    Optional<Book> findById(Long bookId);
    Book findByIdOrThrow(Long bookId);
}
