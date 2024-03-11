package page.clab.api.domain.book.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.book.domain.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByOrderByCreatedAtDesc();

}
