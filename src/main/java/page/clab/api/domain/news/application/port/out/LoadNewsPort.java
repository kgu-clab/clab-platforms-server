package page.clab.api.domain.news.application.port.out;

import page.clab.api.domain.news.domain.News;

import java.util.Optional;

public interface LoadNewsPort {
    Optional<News> findById(Long id);
    News findByIdOrThrow(Long id);
}
