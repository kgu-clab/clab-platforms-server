package page.clab.api.domain.book.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.book.domain.Book;

public interface RetrieveDeletedBooksPort {
    Page<Book> findAllByIsDeletedTrue(Pageable pageable);
}

