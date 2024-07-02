package page.clab.api.domain.news.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.news.domain.News;

public interface RetrieveDeletedNewsPort {
    Page<News> findAllByIsDeletedTrue(Pageable pageable);
}
