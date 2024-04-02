package page.clab.api.domain.book.dao;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.book.domain.Book;

import java.util.List;

public interface BookRepositoryCustom {

    List<Book> findByConditions(String title, String category, String publisher, String borrowerId, String borrowerName, Pageable pageable);

}