package page.clab.api.domain.news.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.news.domain.News;

import java.util.Optional;

public interface RetrieveNewsPort {
    Optional<News> findById(Long id);

    News findByIdOrThrow(Long id);

    Page<News> findAllByIsDeletedTrue(Pageable pageable);

    Page<News> findByConditions(String title, String category, Pageable pageable);
}
