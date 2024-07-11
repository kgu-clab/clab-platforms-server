package page.clab.api.domain.library.book.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookJpaEntity, Long>, BookRepositoryCustom, QuerydslPredicateExecutor<BookJpaEntity> {

    int countByBorrowerId(String memberId);

    @Query(value = "SELECT b.* FROM book b WHERE b.is_deleted = true", nativeQuery = true)
    Page<BookJpaEntity> findAllByIsDeletedTrue(Pageable pageable);
}
