package page.clab.api.domain.library.book.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookRepositoryCustom {
    Page<BookJpaEntity> findByConditions(String title, String category, String publisher, String borrowerId, String borrowerName, Pageable pageable);
}
