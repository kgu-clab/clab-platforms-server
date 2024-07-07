package page.clab.api.domain.book.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import page.clab.api.domain.book.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoryCustom, QuerydslPredicateExecutor<Book> {

    int countByBorrowerId(String memberId);

    @Query(value = "SELECT b.* FROM book b WHERE b.is_deleted = true", nativeQuery = true)
    Page<Book> findAllByIsDeletedTrue(Pageable pageable);
}
