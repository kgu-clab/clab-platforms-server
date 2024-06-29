package page.clab.api.domain.book.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.book.domain.Book;

public interface BookRepositoryCustom {
    Page<Book> findByConditions(String title, String category, String publisher, String borrowerId, String borrowerName, Pageable pageable);
}