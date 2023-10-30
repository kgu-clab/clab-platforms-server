package page.clab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import page.clab.api.type.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
