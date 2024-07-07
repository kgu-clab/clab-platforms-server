package page.clab.api.domain.news.application.port.out;

import page.clab.api.domain.news.domain.News;

public interface RemoveNewsPort {
    void delete(News news);
}
