package page.clab.api.domain.news.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsRepositoryCustom {
    Page<NewsJpaEntity> findByConditions(String title, String category, Pageable pageable);
}
