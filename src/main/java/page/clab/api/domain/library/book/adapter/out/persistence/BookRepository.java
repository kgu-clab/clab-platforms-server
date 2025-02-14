package page.clab.api.domain.library.book.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookJpaEntity, Long>, BookRepositoryCustom,
    QuerydslPredicateExecutor<BookJpaEntity> {

    int countByBorrowerId(String memberId);
}
