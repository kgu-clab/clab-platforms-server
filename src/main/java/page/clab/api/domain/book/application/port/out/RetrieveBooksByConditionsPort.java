package page.clab.api.domain.book.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.book.domain.Book;

public interface RetrieveBooksByConditionsPort {
    Page<Book> findByConditions(String title, String category, String publisher, String borrowerId, String borrowerName, Pageable pageable);
}
