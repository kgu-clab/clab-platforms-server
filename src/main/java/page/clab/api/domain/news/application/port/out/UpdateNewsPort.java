package page.clab.api.domain.news.application.port.out;

import page.clab.api.domain.news.domain.News;

public interface UpdateNewsPort {
    News update(News news);
}
