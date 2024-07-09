package page.clab.api.domain.news.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.news.domain.News;

public interface NewsRepositoryCustom {
    Page<News> findByConditions(String title, String category, Pageable pageable);
}
