package page.clab.api.domain.book.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.domain.book.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findAllByOrderByCreatedAtDesc(Pageable pageable);

}
